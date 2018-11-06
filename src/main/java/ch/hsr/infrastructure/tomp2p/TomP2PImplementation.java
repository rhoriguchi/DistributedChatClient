package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.Inet4Address;

public class TomP2PImplementation implements TomP2P {

    private static final Logger LOGGER = LoggerFactory.getLogger(TomP2PImplementation.class);

    private final MessageHandler messageHandler;
    private final PeerHolder peerHolder;

    public TomP2PImplementation(PeerHolder peerHolder, MessageHandler messageHandler) {
        this.peerHolder = peerHolder;
        this.messageHandler = messageHandler;
    }

    @Override
    public void login(String username) {
        peerHolder.initPeerHolder(username);
        loginHelper(username);
    }

    // TODO bad name
    private void loginHelper(String username) {
        addUsernameToDHT(username);
        messageHandler.initMessageReceivedEventPublisher();
    }

    // TODO add some kind of enum as prefix to mark this as username
    private void addUsernameToDHT(String username) {
        addStringToDHT(Number160.createHash(username), username);
    }

    private void addStringToDHT(Number160 key, String value) {
        try {
            addDataToDHT(key, new Data(value));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void addDataToDHT(Number160 key, Data data) {
        // TODO check if peer initialized

        if (!key.toString().isEmpty()) {
            FuturePut futurePut = peerHolder.getPeerDHT()
                .put(key)
                .data(data)
                .start();

            futurePut.awaitUninterruptibly();
            if (futurePut.isFailed()) {
                // TODO wrong exception
                throw new IllegalArgumentException("Username could not be added to distributed hash table");
            }
        } else {
            throw new IllegalArgumentException("Key can't be empty");
        }
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
        FutureGet futureGet = peerHolder.getPeerDHT().get(peerId).start();
        return futureGet.data().toString();
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
}
