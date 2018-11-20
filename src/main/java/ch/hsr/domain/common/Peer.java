package ch.hsr.domain.common;

import ch.hsr.domain.peer.IpAddress;
import lombok.Data;

@Data
public class Peer {

    private final Username username;
    private final boolean online;
    private final IpAddress ipAddress;

    public static Peer empty() {
        return new Peer(
            Username.empty(),
            false,
            IpAddress.empty()
        );
    }
}
