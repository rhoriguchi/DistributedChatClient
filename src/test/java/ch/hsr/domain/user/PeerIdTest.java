package ch.hsr.domain.user;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class PeerIdTest {

    @Test
    public void isPeerIdTrue() {
        assertThat(PeerId.isPeerId("0x3da541559918a808c2402bba5012f6c60b27661c"), equalTo(true));
    }

    @Test
    public void isPeerIdTrueWithoutOx() {
        assertThat(PeerId.isPeerId("3da541559918a808c2402bba5012f6c60b27661c"), equalTo(true));
    }

    @Test
    public void toStringWith0x() {
        String peerId = "0x3da541559918a808c2402bba5012f6c60b27661c";

        assertThat(PeerId.fromString(peerId).toString(), equalTo(peerId));
    }

    @Test
    public void toStringWithout0x() {
        String peerId = "3da541559918a808c2402bba5012f6c60b27661c";

        assertThat(PeerId.fromString(peerId).toString(), equalTo("0x" + peerId));
    }

    @Test
    public void isPeerIdFalseWithToShortId() {
        assertThat(PeerId.isPeerId("0x3da541559918a808c2402bba5012f6c60b27661"), equalTo(false));
    }

    @Test
    public void isPeerIdFalseWithToLongId() {
        assertThat(PeerId.isPeerId("0x3da541559918a808c2402bba5012f6c60b27661cd"), equalTo(false));
    }

    @Test
    public void isPeerIdFalseWithEmptyString() {
        assertThat(PeerId.isPeerId(""), equalTo(false));
    }

    @Test
    public void isPeerIdFalseWithWrongString() {
        assertThat(PeerId.isPeerId("asdf"), equalTo(false));
    }

}
