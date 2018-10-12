package ch.hsr.infrastructure.tomp2p;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.p2p.Peer;
import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

// TODO maybe use optional for return values
@RequiredArgsConstructor
@Getter
public class PeerObject {

    private final PeerDHT peerDHT;
    private String username;

    public Peer getPeer() {
        if (peerDHT != null) {
            return peerDHT.peer();
        } else {
            return null;
        }
    }

    // TODO wrong place, should be in TomP2P
    public String getUsername() {
        if (peerDHT != null) {
            if (username == null) {
                username = peerDHT.get(getPeerId()).start()
                    .data().toString();
            }

            return username;
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

    public PeerAddress getPeerAddress() {
        if (peerDHT != null) {
            return peerDHT.peerAddress();
        } else {
            return null;
        }
    }
}
