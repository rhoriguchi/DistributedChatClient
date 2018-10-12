package ch.hsr.mapping.user;

import ch.hsr.domain.user.PeerAddress;
import ch.hsr.domain.user.Username;

public interface UserRepository {

    void login(PeerAddress bootstrapPeerAddress, Username username);

    void logout();

}
