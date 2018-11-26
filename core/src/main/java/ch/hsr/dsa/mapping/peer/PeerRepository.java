package ch.hsr.dsa.mapping.peer;

import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.peer.IpAddress;
import ch.hsr.dsa.domain.peer.Peer;

public interface PeerRepository {

    void login(IpAddress bootstrapPeerIpAddress, Username username);

    void logout();

    Peer getSelf();

    Peer get(Username username);
}
