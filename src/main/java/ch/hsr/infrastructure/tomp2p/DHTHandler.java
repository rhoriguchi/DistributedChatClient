package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class DHTHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DHTHandler.class);

    private final PeerHolder peerHolder;

    public DHTHandler(PeerHolder peerHolder) {
        this.peerHolder = peerHolder;
    }

    // TODO add some kind of enum as prefix to mark this as username
    public void addUsername(String username) {
        addString(Number160.createHash(username), username);
    }

    private void addString(Number160 key, String value) {
        try {
            addData(key, new Data(value));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private void addData(Number160 key, Data data) {
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

    // TODO add some kind of enum as prefix to mark this as username
    public String getUsername(Number160 peerId) {
        // TODO check if peer initialized

        FutureGet futureGet = peerHolder.getPeerDHT().get(peerId).start();
        return futureGet.data().toString();
    }
}
