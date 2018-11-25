package ch.hsr.mapping.Util;

import ch.hsr.domain.peer.Peer;
import ch.hsr.infrastructure.tomp2p.message.TomP2PPeerAddress;

public class TomP2PPeerAddressHelper {

    private TomP2PPeerAddressHelper() {
        throw new IllegalAccessError("This class is only for constants");
    }

    public static TomP2PPeerAddress getTomP2PPeerAddress(Peer peer) {
        return new TomP2PPeerAddress(
            peer.getUsername().toString(),
            peer.getIpAddress().toString(),
            peer.getTcpPort().toInteger(),
            peer.getUdpPort().toInteger()
        );
    }
}
