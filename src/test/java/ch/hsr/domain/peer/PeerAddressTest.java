package ch.hsr.domain.peer;

import ch.hsr.domain.peer.peeraddress.InetAddress;
import ch.hsr.domain.peer.peeraddress.PeerAddress;
import ch.hsr.domain.peer.peeraddress.PeerId;
import ch.hsr.domain.peer.peeraddress.Port;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PeerAddressTest {

    @Test
    public void serialize() {
        PeerAddress peerAddress = new PeerAddress(
            PeerId.fromString("0x3da541559918a808c2402bba5012f6c60b27661c"),
            InetAddress.fromString(""),
            Port.fromInteger(4000),
            Port.fromInteger(4000)
        );

        assertThat(PeerAddress.fromSerialize(peerAddress.serialize()), equalTo(peerAddress));
    }

}
