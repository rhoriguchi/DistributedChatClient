package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.peers.Number160;
import java.net.Inet4Address;

public interface TomP2P {

    boolean login(Inet4Address bootstrapInet4Address, String username);

    boolean login(String username);

    void logout();

    // TODO unused
    String getUserName(Number160 peerId);

    PeerObject getSelf();

    // TODO unused
    String getPeerId(String username);

    // TODO unused
    TomP2PMessage getOldestReceivedTomP2PMessage();

    void sendMessage(TomP2PMessage tomP2PMessage);
}
