package ch.hsr.application;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerService.class);

    private final PeerRepository peerRepository;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public PeerService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    public boolean login(IpAddress bootstrapPeerIpAddress, Username username) {
        try {
            peerRepository.login(bootstrapPeerIpAddress, username);
            return true;
            // TODO to broad exception
        } catch (Exception e) {
            // TODO something with this excepting since this means there's a bigger issue
            LOGGER.error(e.getMessage(), e);

            return false;
        }
    }

    public Peer getSelf() {
        return peerRepository.getSelf();
    }
}
