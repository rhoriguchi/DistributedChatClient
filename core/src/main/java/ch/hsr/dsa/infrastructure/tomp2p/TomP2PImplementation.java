package ch.hsr.dsa.infrastructure.tomp2p;

import ch.hsr.dsa.infrastructure.tomp2p.dht.DHTHandler;
import ch.hsr.dsa.infrastructure.tomp2p.dht.DHTScheduler;
import ch.hsr.dsa.infrastructure.tomp2p.message.MessageHandler;
import ch.hsr.dsa.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dsa.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dsa.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dsa.infrastructure.tomp2p.message.TomP2PPeerAddress;
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
    public PeerObject getSelf() {
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
    public Optional<PeerObject> getPeerObject(String username) {
        return dhtHandler.getPeerObject(username);
    }

    @Override
    public TomP2PFriendRequest getOldestReceivedTomP2PFriendRequest() {
        return messageHandler.getOldestReceivedFriendRequest();
    }
}
