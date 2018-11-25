package ch.hsr.application;

import ch.hsr.application.exception.PeerException;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerService.class);

    private final PeerRepository peerRepository;

    public PeerService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    public void login(IpAddress bootstrapPeerIpAddress, Username username) {
        try {
            peerRepository.login(bootstrapPeerIpAddress, username);
            //TODO to broad exception
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new PeerException("Login failed");
        }
    }

    public Peer getSelf() {
        return peerRepository.getSelf();
    }
}
