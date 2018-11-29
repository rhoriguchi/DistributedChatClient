package ch.hsr.dcc.infrastructure.tomp2p.dht;

import ch.hsr.dcc.infrastructure.exception.DHTException;
import ch.hsr.dcc.infrastructure.tomp2p.PeerHolder;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.DHTObjectPrefix;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
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

        TomP2PPeerObject self = peerHolder.getSelf();
        addPeerObject(self, ttl);
    }

    private void addPeerObject(TomP2PPeerObject tomP2PPeerObject, int ttl) {
        try {
            addData(
                getPeerObjectKey(tomP2PPeerObject.getUsername()),
                new Data(tomP2PPeerObject),
                ttl
            );
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("String could not be converted to data");
        }
    }

    private String getPeerObjectKey(String username) {
        return addKeyPrefix(username, DHTObjectPrefix.PEER);
    }

    private String addKeyPrefix(String key, DHTObjectPrefix dhtObjectPrefix) {
        return String.format("%s_%s", dhtObjectPrefix, key);
    }

    private synchronized void addData(String key, Data data, int ttl) {
        if (!key.isEmpty()) {
            if (ttl >= 0) {
                data.ttlSeconds(ttl);
            }

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

    public void addGroupObject(TomP2PGroupObject tomP2PGroupObject) {
        try {
            addData(
                getGroupObjectKey(tomP2PGroupObject.getId()),
                new Data(tomP2PGroupObject),
                -1
            );
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("String could not be converted to data");
        }
    }

    private String getGroupObjectKey(Long id) {
        return addKeyPrefix(id.toString(), DHTObjectPrefix.GROUP);
    }

    public Optional<TomP2PPeerObject> getPeerObject(String username) {
        return getData(getPeerObjectKey(username))
            .map(this::dataToTomP2PPeerObject);
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

    private TomP2PPeerObject dataToTomP2PPeerObject(Data data) {
        try {
            return (TomP2PPeerObject) data.object();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("Distributed hash table data could not be cast to peer object");
        }
    }

    public Optional<TomP2PGroupObject> getGroupObject(Long id) {
        return getData(getGroupObjectKey(id))
            .map(this::dataToTomP2PGroupObject);
    }

    private TomP2PGroupObject dataToTomP2PGroupObject(Data data) {
        try {
            return (TomP2PGroupObject) data.object();
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DHTException("Distributed hash table data could not be cast to group object");
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
