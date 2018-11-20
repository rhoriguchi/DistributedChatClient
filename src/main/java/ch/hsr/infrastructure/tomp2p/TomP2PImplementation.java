package ch.hsr.infrastructure.tomp2p;

import ch.hsr.infrastructure.tomp2p.dht.DHTHandler;
import ch.hsr.infrastructure.tomp2p.message.DefaultTomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.MessageHandler;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.peers.Number160;
import java.net.Inet4Address;
import java.util.Optional;

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
    public PeerObject getSelf() {
        return peerDHTToPeerObject(peerHolder.getPeerDHT());
    }

    private PeerObject peerDHTToPeerObject(PeerDHT peerDHT) {
        return new PeerObject(
            peerDHT.peerID(),
            peerDHT.peer().peerAddress().inetAddress().getHostAddress()
        );
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
    public Optional<String> getPublicKey(String username) {
        return dhtHandler.getPublicKey(username);
    }

    @Override
    public void savePublicKey(String username, String publicKey) {
        dhtHandler.addPublicKey(username, publicKey);
    }

    @Override
    public Optional<String> getUserName(Number160 peerID) {
        return dhtHandler.getUsername(peerID);
    }

    @Override
    public boolean isOnline(Number160 peerID) {
        // TODO mock
        return true;
    }

    @Override
    public Optional<PeerObject> getPeerObject(String username) {
        // TODO mock
        return Optional.empty();
    }
}
