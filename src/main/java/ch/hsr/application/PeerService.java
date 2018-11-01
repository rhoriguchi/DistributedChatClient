package ch.hsr.application;

import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Username;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PeerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerService.class);

    private final PeerRepository peerRepository;

    private final int maxLoginWaitTime;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PeerService(PeerRepository peerRepository, int maxLoginWaitTime) {
        this.peerRepository = peerRepository;
        this.maxLoginWaitTime = maxLoginWaitTime;
    }

    public boolean login(IpAddress bootstrapPeerIpAddress, Username username) {
        try {
            Future<Boolean> future = executorService.submit(() -> peerRepository.login(
                bootstrapPeerIpAddress,
                username
            ));

            return future.get(maxLoginWaitTime, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // TODO something with this excepting since this means there's a bigger issue
            LOGGER.error(e.getMessage(), e);

            return false;
        } finally {
            executorService.shutdown();
        }
    }

    public Set<Peer> getPeers() {
        return peerRepository.getPeers();
    }

    public Peer getSelf() {
        return peerRepository.getSelf();
    }
}
