package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.peers.Number160;
import java.net.Inet4Address;

public class TomP2PImplementation implements TomP2P {

    private final MessageHandler messageHandler;
    private final DHTHandler dhtHandler;
    private final PeerHolder peerHolder;

    public TomP2PImplementation(PeerHolder peerHolder, DHTHandler dhtHandler, MessageHandler messageHandler) {
        this.peerHolder = peerHolder;
        this.dhtHandler = dhtHandler;
        this.messageHandler = messageHandler;
    }

    @Override
    public void login(String username) {
        peerHolder.initPeerHolder(username);
        loginHelper(username);
    }

    // TODO bad name
    private void loginHelper(String username) {
        dhtHandler.addUsername(username);
        messageHandler.initMessageReceivedEventPublisher();
    }

    @Override
    public void login(Inet4Address bootstrapInet4Address, String username) {
        peerHolder.initPeerHolder(bootstrapInet4Address, username);
        loginHelper(username);
    }

    @Override
    public void logout() {
        peerHolder.shutdown();
    }

    @Override
    public String getUserName(Number160 peerId) {
        return dhtHandler.getUsername(peerId);
    }

    @Override
    public PeerObject getSelf() {
        return peerHolder.getPeerObject();
    }

    @Override
    public TomP2PMessage getOldestReceivedTomP2PMessage() {
        return messageHandler.getOldestReceivedTomP2PMessage();
    }

    @Override
    public String getPeerId(String username) {
        return Number160.createHash(username).toString();
    }

    @Override
    public void sendMessage(TomP2PMessage tomP2PMessage) {
        messageHandler.send(tomP2PMessage);
    }

    @Override
    public TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage() {
        return messageHandler.getOldestReceivedTomP2PGroupMessage();
    }
}
