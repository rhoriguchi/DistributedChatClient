package ch.hsr.infrastructure.tomp2p;

import lombok.Data;
import java.io.Serializable;

@Data
public class PeerObject implements Serializable {

    private static final long serialVersionUID = 1543028412446700930L;

    private final String username;
    private final String publicKey;
    private final String ipAddress;
    private final int tcpPort;
    private final int udpPort;
}
