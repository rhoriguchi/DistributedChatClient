package ch.hsr.mapping.peer;

import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Username;
import ch.hsr.domain.peer.peeraddress.InetAddress;
import ch.hsr.domain.peer.peeraddress.PeerAddress;
import ch.hsr.domain.peer.peeraddress.PeerId;
import ch.hsr.domain.peer.peeraddress.Port;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.PeerObject;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.UnknownHostException;

// TODO create username cache
public class PeerMapper implements PeerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerMapper.class);

    private final TomP2P tomP2P;

    public PeerMapper(TomP2P tomP2P) {
        this.tomP2P = tomP2P;
    }

    @Override
    public void login(PeerAddress bootstrapPeerAddress, Username username) {
        tomP2P.login(
            toPeerAddress(bootstrapPeerAddress),
            username.toString()
        );
    }

    // TODO kinda ugly with package
    private net.tomp2p.peers.PeerAddress toPeerAddress(PeerAddress peerAddress) {
        try {
            return new net.tomp2p.peers.PeerAddress(
                toNumber160(peerAddress.getPeerId().toString()),
                peerAddress.getInetAddress().toString(),
                peerAddress.getTcpPort().toInteger(),
                peerAddress.getUdpPort().toInteger()
            );
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException("The host name could not be resolved");
        }
    }

    private Number160 toNumber160(String value) {
        if (value.isEmpty() || value.equals("0")) {
            return new Number160(0);
        } else {
            return new Number160(value);
        }
    }

    @Override
    public void logout() {
        tomP2P.logout();
    }

    private Peer toPeer(PeerObject peerObject) {
        return new Peer(
            Username.fromString(peerObject.getUsername()),
            toPeerAddress(peerObject.getPeerAddress())
        );
    }

    private PeerAddress toPeerAddress(net.tomp2p.peers.PeerAddress peerAddress) {
        return new PeerAddress(
            PeerId.fromString(peerAddress.peerId().toString()),
            InetAddress.fromString(peerAddress.inetAddress().toString()),
            Port.fromInteger(peerAddress.tcpPort()),
            Port.fromInteger(peerAddress.udpPort())
        );
    }
}
