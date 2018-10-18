package ch.hsr.mapping.peer;

import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Username;
import ch.hsr.domain.peer.peeraddress.PeerAddress;
import java.util.Set;

public interface PeerRepository {

    boolean login(PeerAddress bootstrapPeerAddress, Username username);

    void logout();

    Set<Peer> getPeers();

    Peer getSelf();

}
