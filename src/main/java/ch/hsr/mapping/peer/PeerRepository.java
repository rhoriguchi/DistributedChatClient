package ch.hsr.mapping.peer;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;

public interface PeerRepository {

    boolean login(IpAddress bootstrapPeerIpAddress, Username username);

    // TODO unused
    void logout();

    Peer getSelf();
}
