package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.message.DefaultTomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.peers.Number160;
import java.net.Inet4Address;
import java.util.Optional;

public interface TomP2P {

    void login(Inet4Address bootstrapInet4Address, String username);

    void login(String username);

    void logout();

    PeerObject getSelf();

    void sendMessage(DefaultTomP2PMessage defaultTomP2PMessage);

    TomP2PMessage getOldestReceivedTomP2PMessage();

    TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage();

    Optional<String> getPublicKey(String username);

    void savePublicKey(String username, String publicKey);

    Optional<String> getUserName(Number160 peerID);

    boolean isOnline(Number160 peerID);

    Optional<PeerObject> getPeerObject(String username);
}
