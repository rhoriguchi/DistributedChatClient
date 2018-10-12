package ch.hsr.mapping.user;

import ch.hsr.domain.user.InetAddress;
import ch.hsr.domain.user.PeerAddress;
import ch.hsr.domain.user.PeerId;
import ch.hsr.domain.user.Port;
import ch.hsr.domain.user.User;
import ch.hsr.domain.user.Username;
import ch.hsr.infrastructure.tomp2p.DistributedHashTable;
import ch.hsr.infrastructure.tomp2p.PeerObject;
import net.tomp2p.peers.Number160;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.UnknownHostException;

// TODO create username cache
public class UserMapper implements UserRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserMapper.class);

    private final DistributedHashTable distributedHashTable;

    public UserMapper(DistributedHashTable distributedHashTable) {
        this.distributedHashTable = distributedHashTable;
    }

    @Override
    public void login(PeerAddress bootstrapPeerAddress, Username username) {
        distributedHashTable.login(
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
        distributedHashTable.logout();
    }

    private User toUser(PeerObject peerObject) {
        return new User(
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
