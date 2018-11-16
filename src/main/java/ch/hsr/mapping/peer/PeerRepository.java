package ch.hsr.mapping.peer;

import ch.hsr.domain.common.Peer;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.peer.IpAddress;

public interface PeerRepository {

    void login(IpAddress bootstrapPeerIpAddress, Username username);

    // TODO unused
    void logout();

    Peer getSelf();

    Peer getPeer(Username username);
}
