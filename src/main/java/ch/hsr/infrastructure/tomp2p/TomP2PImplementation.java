package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.message.DefaultTomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.MessageHandler;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.dht.PeerDHT;
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
        init(username);
    }

    private void init(String username) {
        dhtHandler.addUsername(username);
        messageHandler.initMessageReceivedEventPublisher();
    }

    @Override
    public void login(Inet4Address bootstrapInet4Address, String username) {
        peerHolder.initPeerHolder(bootstrapInet4Address, username);
        init(username);
    }

    @Override
    public void logout() {
        peerHolder.shutdown();
    }

    @Override
    public PeerDHT getSelf() {
        return peerHolder.getPeerDHT();
    }

    @Override
    public TomP2PMessage getOldestReceivedTomP2PMessage() {
        return messageHandler.getOldestReceivedMessage();
    }

    @Override
    public void sendMessage(DefaultTomP2PMessage defaultTomP2PMessage) {
        messageHandler.send(defaultTomP2PMessage);
    }

    @Override
    public TomP2PGroupMessage getOldestReceivedTomP2PGroupMessage() {
        return messageHandler.getOldestReceivedGroupMessage();
    }

    @Override
    public String getPublicKey(String username) {
        return dhtHandler.getPublicKey(username);
    }

    @Override
    public void savePublicKey(String username, String publicKey) {
        dhtHandler.addPublicKey(username, publicKey);
    }
}
