package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.Data;

@Data
public class TomP2PPeerAddress {

    private final String username;
    private final String ipAddress;
    private final int tcpPort;
    private final int udpPort;
}
