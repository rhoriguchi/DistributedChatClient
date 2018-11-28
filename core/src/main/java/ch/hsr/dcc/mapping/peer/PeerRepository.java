package ch.hsr.dcc.mapping.peer;

import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.peer.IpAddress;
import ch.hsr.dcc.domain.peer.Peer;

public interface PeerRepository {

    void login(IpAddress bootstrapPeerIpAddress, Username username);

    void logout();

    Peer getSelf();

    Peer get(Username username);
}
