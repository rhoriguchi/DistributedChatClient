package ch.hsr.infrastructure.tomp2p;

import lombok.Data;
import net.tomp2p.peers.Number160;

@Data
public class PeerObject {

    private final Number160 peerId;
    private final String ipAddress;
}
