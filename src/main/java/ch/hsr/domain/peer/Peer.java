package ch.hsr.domain.peer;

import ch.hsr.domain.common.PeerId;
import ch.hsr.domain.common.Username;
import lombok.Data;

@Data
public class Peer {

    private final PeerId peerId;
    private final Username username;
    private final IpAddress ipAddress;

}
