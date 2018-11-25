package ch.hsr.domain.peer;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class IpAddressTest {

    @Test
    public void isIpAddressTrue() {
        assertThat(IpAddress.isValidIpAddress("192.168.1.1"), equalTo(true));
    }

    @Test
    public void isIpAddressFalseWithToShortIp() {
        assertThat(IpAddress.isValidIpAddress("192.168.1"), equalTo(false));
    }

    @Test
    public void isIpAddressFalseWithToLongIp() {
        assertThat(IpAddress.isValidIpAddress("192.168.1.1.1"), equalTo(false));
    }

    @Test
    public void isIpAddressFalseWithEmptyString() {
        assertThat(IpAddress.isValidIpAddress(""), equalTo(false));
    }

    @Test
    public void isIpAddressFalseWithWrongString() {
        assertThat(IpAddress.isValidIpAddress("asdf"), equalTo(false));
    }

}
