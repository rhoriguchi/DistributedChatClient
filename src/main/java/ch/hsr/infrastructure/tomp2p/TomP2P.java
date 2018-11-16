package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.message.DefaultTomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.dht.PeerDHT;
import java.net.Inet4Address;

public interface TomP2P {

    void login(Inet4Address bootstrapInet4Address, String username);

    void login(String username);

    void logout();

    PeerDHT getSelf();

    TomP2PMessage getOldestReceivedMessage();

    void sendMessage(DefaultTomP2PMessage defaultTomP2PMessage);

    TomP2PGroupMessage getOldestReceivedGroupMessage();
}
