package ch.hsr.infrastructure.tomp2p.dht;

import ch.hsr.infrastructure.exception.DHTException;
import ch.hsr.infrastructure.tomp2p.PeerHolder;
import ch.hsr.infrastructure.tomp2p.PeerObject;
import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.PutBuilder;
import net.tomp2p.p2p.JobScheduler;
import net.tomp2p.p2p.Shutdown;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class DHTHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DHTHandler.class);

    private final PeerHolder peerHolder;

    private final int ttl;
    private final int replicationInterval;

    private volatile Queue<PutBuilder> putBuilders = new LinkedList();

    public DHTHandler(PeerHolder peerHolder, int ttl, int replicationInterval) {
        this.peerHolder = peerHolder;
        this.ttl = ttl;
        this.replicationInterval = replicationInterval;
    }

    public void updateSelf() {
        LOGGER.debug("Updating self in distributed hash table");

        PeerObject self = peerHolder.getSelf();
        addPeerObject(self.getUsername(), self, ttl);
    }

    private synchronized void addPeerObject(String key, PeerObject peerObject, int ttl) {
        try {
            addData(key, new Data(peerObject), ttl);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("String could not be converted to data");
        }
    }

    private void addData(String key, Data data, int ttl) {
        if (!key.isEmpty()) {
            if (ttl >= 0) {
                data.ttlSeconds(ttl);
            }

            //TODO test
            if (putBuilders.stream().anyMatch(putBuilder -> putBuilder.data().getValue().equals(data))) {
                PutBuilder putBuilder = peerHolder.getPeerDHT()
                    .put(Number160.createHash(key))
                    .data(data);

                putBuilders.add(putBuilder);
            }
        } else {
            throw new DHTException("Key can't be empty");
        }
    }

    public Optional<PeerObject> getPeerObject(String username) {
        return getData(username)
            .map(this::dateToPeerObject);
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

    private PeerObject dateToPeerObject(Data data) {
        try {
            return (PeerObject) data.object();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("Distributed hash table data could not be cast to peerObject");
        }
    }

    public synchronized void startReplication() {
        LOGGER.debug("Starting distributed hash table replication...");

        Queue<PutBuilder> putBuilders = new LinkedList<>(this.putBuilders);
        this.putBuilders.removeAll(putBuilders);

        putBuilders.stream().map(putBuilder -> {
                Shutdown replication = new JobScheduler(peerHolder.getPeer())
                    .start(putBuilder, replicationInterval, 1);

                try {
                    Thread.sleep(10_000);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }

                return replication.shutdown();
            }
        ).forEach(baseFuture -> {
            baseFuture.awaitUninterruptibly();
            if (baseFuture.isFailed()) {
                throw new DHTException("Distributed hash table data could not be replicated");
            }
        });

        LOGGER.debug("Done replicating distributed hash table");
    }
}
