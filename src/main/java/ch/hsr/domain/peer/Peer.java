package ch.hsr.domain.peer;

import ch.hsr.domain.peer.peeraddress.PeerAddress;
import lombok.Data;

@Data
public class Peer {

    private final Username username;
    private final PeerAddress peerAddress;
}
