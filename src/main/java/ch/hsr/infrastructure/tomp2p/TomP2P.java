package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;
import java.net.Inet4Address;

public interface TomP2P {

    void login(Inet4Address bootstrapInet4Address, String username);

    void login(String username);

    void logout();

    // TODO unused
    String getUserName(Number160 peerId);

    PeerDHT getSelf();

    // TODO unused
    String getPeerId(String username);

    TomP2PMessage getOldestReceivedMessage();

    void sendMessage(TomP2PMessage tomP2PMessage);

    TomP2PGroupMessage getOldestReceivedGroupMessage();
}
