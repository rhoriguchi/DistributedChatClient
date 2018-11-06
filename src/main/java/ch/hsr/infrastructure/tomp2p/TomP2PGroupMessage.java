package ch.hsr.infrastructure.tomp2p;

import lombok.Data;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class TomP2PGroupMessage implements Serializable {

    private static final long serialVersionUID = -3638520781895726808L;

    private final Long id;
    private final String fromUsername;
    private final Long toGroupId;
    private final String text;
    private final String timeStamp;
    private final Map<String, Boolean> received;

    public TomP2PGroupMessage(Long id,
                              String fromUsername,
                              Long toGroupId,
                              String text,
                              String timeStamp,
                              Map<String, Boolean> received) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toGroupId = toGroupId;
        this.text = text;
        this.timeStamp = timeStamp;
        this.received = new HashMap<>(received);
    }

    public Map<String, Boolean> getReceived() {
        return new HashMap<>(received);
    }
}
