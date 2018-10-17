package ch.hsr.domain.peer.peeraddress;

import ch.hsr.domain.common.StringValue;

public class InetAddress extends StringValue {

    private static final long serialVersionUID = -3459986319975961745L;

    private InetAddress(String inetAddress) {
        super(inetAddress);
    }

    public static InetAddress fromString(String inetAddress) {
        return new InetAddress(inetAddress);
    }

    public static InetAddress empty() {
        return new InetAddress("");
    }
}

