package ch.hsr.dcc.infrastructure.tomp2p.dht.object;

import lombok.Data;
import java.io.Serializable;

@Data
//TODO add signature?
public class TomP2PPeerObject implements Serializable {

    private static final long serialVersionUID = 5810427451596436279L;

    private final String username;
    private final String ipAddress;
    private final int tcpPort;
    private final int udpPort;
}
