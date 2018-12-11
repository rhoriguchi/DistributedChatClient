package ch.hsr.dcc.infrastructure.tomp2p;

import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupAdd;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PPeerAddress;
import java.net.Inet4Address;
import java.util.Optional;

public interface TomP2P {

    void login(String username);

    void login(Inet4Address bootstrapInet4Address, String username);

    void logout();

    TomP2PPeerObject getSelf();

    void sendMessage(TomP2PMessage tomP2PMessage, TomP2PPeerAddress tomP2PPeerAddress);

    void sendFriendRequest(TomP2PFriendRequest tomP2PFriendRequest, TomP2PPeerAddress tomP2PPeerAddress);

    TomP2PMessage getOldestReceivedTomP2PMessage();

    TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage();

    Optional<TomP2PPeerObject> getPeerObject(String username);

    TomP2PFriendRequest getOldestReceivedTomP2PFriendRequest();

    Optional<TomP2PGroupObject> getGroupObject(Long id);

    void addGroupObject(TomP2PGroupObject tomP2PGroupObject);

    void sendGroupAdd(TomP2PGroupAdd groupToTomP2PGroupAdd, TomP2PPeerAddress tomP2PPeerAddress);

    TomP2PGroupAdd getOldestReceivedTomP2PGroupAdd();
}
