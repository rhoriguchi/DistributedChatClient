package ch.hsr.dsa.application;

import ch.hsr.dsa.application.exception.PeerException;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.peer.IpAddress;
import ch.hsr.dsa.domain.peer.Peer;
import ch.hsr.dsa.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

public class PeerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerService.class);

    private final PeerRepository peerRepository;

    public PeerService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    @Async
    public void login(IpAddress bootstrapPeerIpAddress, Username username) {
        if (!username.isEmpty()) {
            try {
                peerRepository.login(bootstrapPeerIpAddress, username);
                //TODO to broad exception
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                throw new PeerException("Login failed");
            }
        } else {
            throw new PeerException("Login username can't be empty");
        }
    }

    public Peer getSelf() {
        return peerRepository.getSelf();
    }
    
    @Async
    public void logout() {
        peerRepository.logout();
    }
}
