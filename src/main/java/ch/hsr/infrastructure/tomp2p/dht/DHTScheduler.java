package ch.hsr.infrastructure.tomp2p.dht;

import org.springframework.scheduling.annotation.Scheduled;

public class DHTScheduler {

    private final DHTHandler dhtHandler;

    private boolean replicationEnabled = false;
    private boolean updateSelfEnable = false;

    public DHTScheduler(DHTHandler dhtHandler) {
        this.dhtHandler = dhtHandler;
    }

    @Scheduled (fixedDelay = 10_000)
    public synchronized void updateSelf() {
        if (updateSelfEnable) {
            dhtHandler.updateSelf();
        }
    }

    @Scheduled (fixedDelay = 1_000)
    public synchronized void startReplication() {
        if (replicationEnabled) {
            dhtHandler.startReplication();
        }
    }

    public synchronized void setReplicationEnabled(boolean replicationEnabled) {
        this.replicationEnabled = replicationEnabled;
    }

    public synchronized void setUpdateSelfEnable(boolean updateSelfEnable) {
        this.updateSelfEnable = updateSelfEnable;
    }
}
