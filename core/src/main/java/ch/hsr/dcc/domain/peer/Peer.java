package ch.hsr.dcc.domain.peer;

import ch.hsr.dcc.domain.common.Username;
import lombok.Data;

@Data
public class Peer {

    private final Username username;
    private final IpAddress ipAddress;
    private final Port tcpPort;
    private final Port udpPort;
    private final boolean online;
}
