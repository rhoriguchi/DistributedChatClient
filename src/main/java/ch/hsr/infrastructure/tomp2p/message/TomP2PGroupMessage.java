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
public class TomP2PGroupMessage extends DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = 8998056888277933720L;

    private final Long toGroupId;
    private Map<String, String> states;

    public TomP2PGroupMessage(Long id,
                              String fromUsername,
                              String toUsername,
                              Long toGroupId,
                              String text,
                              String timeStamp,
                              String signature,
                              Map<String, String> states) {
        super(id, fromUsername, toUsername, text, timeStamp, signature);
        this.toGroupId = toGroupId;
        this.states = new HashMap<>(states);
    }

    public Map<String, String> getStates() {
        return new HashMap<>(states);
    }

    public void setStates(Map<String, String> states) {
        this.states = new HashMap<>(states);
    }
}
