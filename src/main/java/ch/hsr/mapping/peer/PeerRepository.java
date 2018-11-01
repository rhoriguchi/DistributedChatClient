package ch.hsr.mapping.peer;

import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Username;
import java.util.Set;

public interface PeerRepository {

    boolean login(IpAddress bootstrapPeerIpAddress, Username username);

    void logout();

    Set<Peer> getPeers();

    Peer getSelf();

}
