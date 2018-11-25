package ch.hsr.infrastructure.tomp2p;

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

    //TODO rename since errors are in same que
    TomP2PMessage getOldestReceivedTomP2PMessage();

    //TODO rename since errors are in same que
    TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage();

    Optional<PeerObject> getPeerObject(String username);
}
