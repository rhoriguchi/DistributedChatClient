package ch.hsr.mapping.peer;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;

public interface PeerRepository {

    void login(IpAddress bootstrapPeerIpAddress, Username username);

    void logout();

    Peer getSelf();

    Peer get(Username username);
}
