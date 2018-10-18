package ch.hsr.application;

import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Username;
import ch.hsr.domain.peer.peeraddress.PeerAddress;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PeerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerService.class);

    private final PeerRepository peerRepository;

    private final int maxLoginWaitTime;

    private final ExecutorService service = Executors.newSingleThreadExecutor();

    public PeerService(PeerRepository peerRepository, int maxLoginWaitTime) {
        this.peerRepository = peerRepository;
        this.maxLoginWaitTime = maxLoginWaitTime;
    }

    public boolean login(PeerAddress bootstrapPeerAddress, Username username) {
        try {
            Future<Boolean> future = service.submit(() -> peerRepository.login(bootstrapPeerAddress, username));

            return future.get(maxLoginWaitTime, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // TODO something with this excepting since this means there's a bigger issue
            LOGGER.error(e.getMessage(), e);

            return false;
        } finally {
            service.shutdown();
        }
    }

    // TODO get real peer object collection
    public Set<Peer> getPeers() {
        return IntStream.rangeClosed(0, 100)
            .mapToObj(String::valueOf)
            .map(username -> new Peer(
                Username.fromString(username),
                PeerAddress.empty()
            )).collect(Collectors.toSet());
    }

    // TODO need to get this from a deeper layer after the login happens
    public Peer getSelf() {
        return new Peer(
            Username.fromString("Mock"),
            PeerAddress.empty()
        );
    }
}
