package ch.hsr.infrastructure.tomp2p;

import lombok.Data;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Data
public class TomP2PGroupMessage implements Serializable {

    private static final long serialVersionUID = 426396371040837030L;

    private final Long id;
    private final String fromUsername;
    private final Long toGroupId;
    private final String text;
    private final String timeStamp;
    private final Map<String, Boolean> received;
    private final String signature;

    public TomP2PGroupMessage(Long id,
                              String fromUsername,
                              Long toGroupId,
                              String text,
                              String timeStamp,
                              Map<String, Boolean> received,
                              String signature) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toGroupId = toGroupId;
        this.text = text;
        this.timeStamp = timeStamp;
        this.received = new HashMap<>(received);
        this.signature = signature;
    }

    public Map<String, Boolean> getReceived() {
        return new HashMap<>(received);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TomP2PGroupMessage that = (TomP2PGroupMessage) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(fromUsername, that.fromUsername) &&
            Objects.equals(toGroupId, that.toGroupId) &&
            Objects.equals(text, that.text) &&
            Objects.equals(timeStamp, that.timeStamp) &&
            Objects.equals(received, that.received);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUsername, toGroupId, text, timeStamp, received);
    }
}
