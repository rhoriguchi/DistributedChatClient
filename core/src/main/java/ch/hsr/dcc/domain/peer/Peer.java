package ch.hsr.dcc.domain.peer;

import ch.hsr.dcc.domain.common.Username;
import lombok.Data;
import java.util.Objects;

@Data
public class Peer {

    private final Username username;
    private final IpAddress ipAddress;
    private final Port tcpPort;
    private final Port udpPort;
    private final boolean online;

    public static Peer empty() {
        return new Peer(
            Username.empty(),
            IpAddress.empty(),
            Port.empty(),
            Port.empty(),
            false
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Peer peer = (Peer) o;
        return online == peer.online &&
            Objects.equals(username, peer.username) &&
            Objects.equals(ipAddress, peer.ipAddress) &&
            Objects.equals(tcpPort, peer.tcpPort) &&
            Objects.equals(udpPort, peer.udpPort);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
