package ch.hsr.dcc.application;

import ch.hsr.dcc.application.exception.PeerException;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.peer.IpAddress;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.event.login.LoginEventPublisher;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import javax.annotation.PreDestroy;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

public class PeerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerService.class);

    private final PeerRepository peerRepository;
    private final LoginEventPublisher loginEventPublisher;

    private final Semaphore loginLock = new Semaphore(1);
    private final Semaphore logoutLock = new Semaphore(1);
    private final AtomicBoolean loggedIn = new AtomicBoolean(false);

    public PeerService(PeerRepository peerRepository, LoginEventPublisher loginEventPublisher) {
        this.peerRepository = peerRepository;
        this.loginEventPublisher = loginEventPublisher;
    }

    @Async
    public void login(IpAddress bootstrapPeerIpAddress, Username username) {
        if (!loggedIn.get() && loginLock.tryAcquire()) {
            if (!username.isEmpty()) {
                try {
                    peerRepository.login(bootstrapPeerIpAddress, username);
                    loginEventPublisher.loginSuccess();

                    loggedIn.set(true);
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    loginEventPublisher.loginFailed();
                } finally {
                    loginLock.release();
                }
            } else {
                loginLock.release();

                throw new PeerException("Login username can't be empty");
            }
        }
    }

    public Peer getSelf() {
        return peerRepository.getSelf();
    }

    @Async
    @PreDestroy
    public void logout() {
        if (loggedIn.get() && logoutLock.tryAcquire()) {
            peerRepository.logout();

            loggedIn.set(false);
            logoutLock.release();
        }
    }
}
