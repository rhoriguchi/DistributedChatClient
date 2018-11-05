package ch.hsr.mapping.peer;

import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.PeerId;
import ch.hsr.domain.peer.Username;

public interface PeerRepository {

    boolean login(IpAddress bootstrapPeerIpAddress, Username username);

    // TODO unused
    void logout();

    Peer getSelf();

    PeerId getPeerId(Username username);
}
