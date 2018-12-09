package ch.hsr.dcc.infrastructure.tomp2p;

import ch.hsr.dcc.infrastructure.tomp2p.dht.DHTHandler;
import ch.hsr.dcc.infrastructure.tomp2p.dht.DHTScheduler;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.MessageHandler;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupAdd;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PPeerAddress;
import java.net.Inet4Address;
import java.util.Optional;

public class TomP2PImplementation implements TomP2P {

    private final PeerHolder peerHolder;
    private final DHTHandler dhtHandler;
    private final DHTScheduler dhtScheduler;
    private final MessageHandler messageHandler;

    public TomP2PImplementation(PeerHolder peerHolder, DHTHandler dhtHandler, DHTScheduler dhtScheduler, MessageHandler messageHandler) {
        this.peerHolder = peerHolder;
        this.dhtHandler = dhtHandler;
        this.dhtScheduler = dhtScheduler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void login(String username, String publicKey) {
        peerHolder.initPeerHolder(username, publicKey);
        init();
    }

    private void init() {
        messageHandler.initMessageReceivedEventPublisher();

        dhtScheduler.setUpdateSelfEnable(true);
        dhtScheduler.setReplicationEnabled(true);
    }

    @Override
    public void login(Inet4Address bootstrapInet4Address, String username, String publicKey) {
        peerHolder.initPeerHolder(bootstrapInet4Address, username, publicKey);
        init();
    }

    @Override
    public void logout() {
        peerHolder.shutdown();

        dhtScheduler.setUpdateSelfEnable(false);
        dhtScheduler.setReplicationEnabled(false);
    }

    @Override
    public TomP2PPeerObject getSelf() {
        return peerHolder.getSelf();
    }

    @Override
    public TomP2PMessage getOldestReceivedTomP2PMessage() {
        return messageHandler.getOldestReceivedMessage();
    }

    @Override
    public void sendMessage(TomP2PMessage tomP2PMessage, TomP2PPeerAddress tomP2PPeerAddress) {
        messageHandler.send(tomP2PMessage, tomP2PPeerAddress);
    }

    @Override
    public void sendFriendRequest(TomP2PFriendRequest tomP2PFriendRequest, TomP2PPeerAddress tomP2PPeerAddress) {
        messageHandler.send(tomP2PFriendRequest, tomP2PPeerAddress);
    }

    @Override
    public TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage() {
        return messageHandler.getOldestReceivedGroupMessage();
    }

    @Override
    public Optional<TomP2PPeerObject> getPeerObject(String username) {
        return dhtHandler.getPeerObject(username);
    }

    @Override
    public TomP2PFriendRequest getOldestReceivedTomP2PFriendRequest() {
        return messageHandler.getOldestReceivedFriendRequest();
    }

    @Override
    public Optional<TomP2PGroupObject> getGroupObject(Long id) {
        return dhtHandler.getGroupObject(id);
    }

    @Override
    public void addGroupObject(TomP2PGroupObject tomP2PGroupObject) {
        dhtHandler.addGroupObject(tomP2PGroupObject);
    }

    @Override
    public void sendGroupAdd(TomP2PGroupAdd groupToTomP2PGroupAdd, TomP2PPeerAddress tomP2PPeerAddress) {
        messageHandler.sendGroupAdd(groupToTomP2PGroupAdd, tomP2PPeerAddress);
    }

    @Override
    public TomP2PGroupAdd getOldestReceivedTomP2PGroupAdd() {
        return messageHandler.getOldestReceivedGroupAdd();
    }
}
