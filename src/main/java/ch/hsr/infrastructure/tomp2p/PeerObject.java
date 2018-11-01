package ch.hsr.infrastructure.tomp2p;

import lombok.Data;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;

@Data
public class PeerObject {

    private final PeerDHT peerDHT;

    public Peer getPeer() {
        if (peerDHT != null) {
            return peerDHT.peer();
        } else {
            return null;
        }
    }

    public Number160 getPeerId() {
        if (peerDHT != null) {
            return peerDHT.peerID();
        } else {
            return null;
        }
    }

    public String getIpAddress() {
        if (peerDHT != null) {
            return peerDHT.peer().peerAddress().inetAddress().getHostAddress();
        } else {
            return null;
        }
    }
}
