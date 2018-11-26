package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PPeerAddress;
import java.net.Inet4Address;
import java.util.Optional;

public interface TomP2P {

    void login(String username, String publicKey);

    void login(Inet4Address bootstrapInet4Address, String username, String publicKey);

    void logout();

    PeerObject getSelf();

    void sendMessage(TomP2PMessage tomP2PMessage, TomP2PPeerAddress tomP2PPeerAddress);

    void sendFriendRequest(TomP2PFriendRequest tomP2PFriendRequest, TomP2PPeerAddress tomP2PPeerAddress);

    TomP2PMessage getOldestReceivedTomP2PMessage();

    TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage();

    Optional<PeerObject> getPeerObject(String username);

    TomP2PFriendRequest getOldestReceivedTomP2PFriendRequest();
}
