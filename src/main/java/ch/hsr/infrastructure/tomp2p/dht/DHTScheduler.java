package ch.hsr.infrastructure.tomp2p.dht;

import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;

@Setter
public class DHTScheduler {

    private final DHTHandler dhtHandler;

    private boolean replicationEnabled = false;
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
