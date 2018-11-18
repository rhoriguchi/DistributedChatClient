package ch.hsr.infrastructure.tomp2p;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class TomP2PMessage implements Serializable {

    private static final long serialVersionUID = 3341954415252268415L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String messageTimeStamp;
    private boolean received;
    private final String signature;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TomP2PMessage that = (TomP2PMessage) o;
        return received == that.received &&
            Objects.equals(id, that.id) &&
            Objects.equals(fromUsername, that.fromUsername) &&
            Objects.equals(toUsername, that.toUsername) &&
            Objects.equals(text, that.text) &&
            Objects.equals(messageTimeStamp, that.messageTimeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUsername, toUsername, text, messageTimeStamp, received);
    }
}
