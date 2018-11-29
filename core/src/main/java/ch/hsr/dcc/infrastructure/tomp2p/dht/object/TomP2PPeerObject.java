package ch.hsr.dcc.infrastructure.tomp2p.dht.object;

import lombok.Data;
import java.io.Serializable;

@Data
//TODO add signature?
public class TomP2PPeerObject implements Serializable {

    private static final long serialVersionUID = 1543028412446700930L;

    private final String username;
    private final String publicKey;
    private final String ipAddress;
    private final int tcpPort;
    private final int udpPort;
}
