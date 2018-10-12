package ch.hsr.domain.user;

import lombok.Data;
import net.tomp2p.p2p.Peer;

@Data
public class User {

    private final Username username;
    private final PeerAddress peerAddress;
}
