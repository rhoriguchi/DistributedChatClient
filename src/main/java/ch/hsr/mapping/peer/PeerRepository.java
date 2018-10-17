package ch.hsr.mapping.peer;

import ch.hsr.domain.peer.Username;
import ch.hsr.domain.peer.peeraddress.PeerAddress;

public interface PeerRepository {

    void login(PeerAddress bootstrapPeerAddress, Username username);

    void logout();

}
