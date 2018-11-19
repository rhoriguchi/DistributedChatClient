package ch.hsr.mapping.peer;

import ch.hsr.domain.common.Peer;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.peer.IpAddress;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import net.tomp2p.dht.PeerDHT;
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
    public void login(IpAddress bootstrapPeerIpAddress, Username username) {
        if (bootstrapPeerIpAddress.isEmpty()) {
            tomP2P.login(username.toString());
        } else {
            tomP2P.login(
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
        return peerDHTToPeer(tomP2P.getSelf());
    }

    private Peer peerDHTToPeer(PeerDHT peerDHT) {
        return new Peer(
            getUsername(peerDHT),
            getState(peerDHT),
            getIpAddress(peerDHT)
        );
    }

    private boolean getState(PeerDHT peerDHT) {
        // TODO mock
        return true;
    }

    // TODO find better solution?
    private IpAddress getIpAddress(PeerDHT peerDHT) {
        return IpAddress.fromString(peerDHT.peer().peerAddress().inetAddress().getHostAddress());
    }

    private Username getUsername(PeerDHT peerDHT) {
        // TODO commented
//        return Username.fromString(tomP2P.getUserName(peerDHT.peerID()));
        return Username.fromString("asdf");
    }

    @Override
    public Peer getPeer(Username username) {
        // TODO mock
        return new Peer(
            Username.empty(),
            true,
            IpAddress.empty()
        );
    }
}
