package ch.hsr.mapping.peer;

import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.PeerId;
import ch.hsr.domain.peer.Username;
import ch.hsr.infrastructure.tomp2p.PeerObject;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Inet4Address;
import java.net.UnknownHostException;


public class PeerMapper implements PeerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerMapper.class);

    private final TomP2P tomP2P;

    public PeerMapper(TomP2P tomP2P) {
        this.tomP2P = tomP2P;
    }

    @Override
    public boolean login(IpAddress bootstrapPeerIpAddress, Username username) {
        if (bootstrapPeerIpAddress.isEmpty()) {
            return tomP2P.login(username.toString());
        } else {
            return tomP2P.login(
                ipAddressToInet4Address(bootstrapPeerIpAddress),
                username.toString()
            );
        }
    }

    private Inet4Address ipAddressToInet4Address(IpAddress ipAddress) {
        try {
            return (Inet4Address) Inet4Address.getByName(ipAddress.toString());
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);

            throw new IllegalArgumentException("IPv4 Address is not valid");
        }
    }

    @Override
    public void logout() {
        tomP2P.logout();
    }

    @Override
    public Peer getSelf() {
        // TODO mock
        return new Peer(
            PeerId.empty(),
            Username.fromString("Mock"),
            IpAddress.empty()
        );
    }

    @Override
    public PeerId getPeerId(Username username) {
        return PeerId.fromString(tomP2P.getPeerId(username.toString()));
    }

    // TODO not used
    private Peer toPeer(PeerObject peerObject) {
        return new Peer(
            PeerId.fromString(peerObject.getPeerId().toString()),
            getUsername(peerObject),
            IpAddress.fromString(peerObject.getIpAddress())
        );
    }


    public Username getUsername(PeerObject peerObject) {
        String username = tomP2P.getUserName(peerObject.getPeerId());
        return Username.fromString(username);
    }
}
