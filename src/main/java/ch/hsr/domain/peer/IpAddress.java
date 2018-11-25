package ch.hsr.domain.peer;

import ch.hsr.Constants;
import ch.hsr.domain.common.StringValue;
import com.google.common.net.InetAddresses;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

public class IpAddress extends StringValue {

    private static final long serialVersionUID = -9153648402742146590L;

    private IpAddress(String ipAddress) {
        super(ipAddress);
    }

    public static IpAddress fromString(String ipAddress) {
        return !isNullOrEmpty(ipAddress) ? parseIpAddress(ipAddress) : empty();
    }

    private static IpAddress parseIpAddress(String ipAddress) {
        checkArgument(isIpAddress(ipAddress), "Invalid IPv4 Address '%s'", ipAddress);
        return new IpAddress(ipAddress);
    }

    public static boolean isIpAddress(String ipAddress) {
        return Pattern.compile(Constants.IP_ADDRESS_PATTERN).matcher(ipAddress).matches()
            && InetAddresses.isInetAddress(ipAddress);
    }

    public static IpAddress empty() {
        return new IpAddress("");
    }
}

