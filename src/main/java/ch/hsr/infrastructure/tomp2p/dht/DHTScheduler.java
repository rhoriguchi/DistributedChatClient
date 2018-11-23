package ch.hsr.infrastructure.tomp2p.dht;

import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

public class DHTScheduler {

    private final DHTHandler dhtHandler;

    @Setter
    private boolean replicationEnabled = false;
    @Setter
    private boolean updateSelfEnable = false;

    public DHTScheduler(DHTHandler dhtHandler) {
        this.dhtHandler = dhtHandler;
    }

    @Scheduled (fixedDelay = 10_000)
    public void updateSelf() {
        if (updateSelfEnable) {
            dhtHandler.updateSelf();
        }
    }

    @Scheduled (fixedDelay = 1_000)
    public void startReplication() {
        if (replicationEnabled) {
            dhtHandler.startReplication();
        }
    }
}
