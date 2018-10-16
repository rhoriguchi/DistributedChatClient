package ch.hsr.domain.user;

import lombok.Data;

@Data
public class User {

    private final Username username;
    private final PeerAddress peerAddress;
}
