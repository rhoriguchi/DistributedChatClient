package ch.hsr.infrastructure.tomp2p.dht;

import ch.hsr.infrastructure.exception.DHTException;
import ch.hsr.infrastructure.tomp2p.PeerHolder;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.Optional;

public class DHTHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DHTHandler.class);

    private final PeerHolder peerHolder;

    public DHTHandler(PeerHolder peerHolder) {
        this.peerHolder = peerHolder;
    }

    public void addUsername(String username) {
        String key = addPrefix(DHTDataType.USERNAME, Number160.createHash(username).toString());
        addString(key, username);
    }

    private String addPrefix(DHTDataType dhtDataType, String string) {
        return String.format("%s_%s", dhtDataType.name(), string);
    }

    private void addString(String key, String value) {
        try {
            addData(key, new Data(value));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void addData(String key, Data data) {
        if (!key.isEmpty()) {
            FuturePut futurePut = peerHolder.getPeerDHT()
                .put(Number160.createHash(key))
                .data(data)
                .start();

            futurePut.awaitUninterruptibly();
            if (futurePut.isFailed()) {
                throw new DHTException("Data could not be added to distributed hash table");
            }
        } else {
            throw new DHTException("Key can't be empty");
        }
    }

    public Optional<String> getUsername(Number160 peerId) {
        String key = addPrefix(DHTDataType.USERNAME, peerId.toString());
        return getData(key)
            .map(this::dataToString);
    }

    private Optional<Data> getData(String key) {
        if (!key.isEmpty()) {
            FutureGet futureGet = peerHolder.getPeerDHT()
                .get(Number160.createHash(key))
                .start();

            futureGet.awaitUninterruptibly();
            if (futureGet.isSuccess()) {
                return Optional.ofNullable(futureGet.data());
            } else {
                return Optional.empty();
            }
        } else {
            throw new DHTException("Key can't be empty");
        }
    }

    private String dataToString(Data data) {
        try {
            return (String) data.object();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("Distributed hash table data could not be cast to string");
        }
    }

    public Optional<String> getPublicKey(String username) {
        String key = addPrefix(DHTDataType.PUBLIC_KEY, username);
        return getData(key)
            .map(this::dataToString);
    }

    public void addPublicKey(String username, String publicKey) {
        String key = addPrefix(DHTDataType.PUBLIC_KEY, username);
        addString(key, publicKey);
    }
}
