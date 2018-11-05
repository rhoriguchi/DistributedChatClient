package ch.hsr.domain.peer;

import lombok.Data;

@Data
public class Peer {

    private final PeerId peerId;
    private final Username username;
    private final IpAddress ipAddress;

    public static Peer newPeer(PeerId peerId, Username username) {
        return new Peer(
            peerId,
            username,
            IpAddress.empty()
        );
    }
}
