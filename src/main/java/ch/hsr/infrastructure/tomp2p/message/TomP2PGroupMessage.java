package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PGroupMessage extends TomP2PDefaultMessage implements Serializable {

    private static final long serialVersionUID = -1760102195108529978L;

    private final Long id;
    private final String fromUsername;
    private final Long toGroupId;
    private final String text;
    private final String timeStamp;
    private final Map<String, Boolean> received;

    public TomP2PGroupMessage(TomP2PDefaultMessageState state,
                              Long id,
                              String fromUsername,
                              Long toGroupId,
                              String text,
                              String timeStamp,
                              Map<String, Boolean> received) {
        super(state);
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
