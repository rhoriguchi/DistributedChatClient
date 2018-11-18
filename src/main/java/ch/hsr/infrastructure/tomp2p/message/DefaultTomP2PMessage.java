package ch.hsr.infrastructure.tomp2p.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = -4293137448173671014L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String timeStamp;
    private final String signature;
    private TomP2PMessageState state;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultTomP2PMessage that = (DefaultTomP2PMessage) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(fromUsername, that.fromUsername) &&
            Objects.equals(toUsername, that.toUsername) &&
            Objects.equals(text, that.text) &&
            Objects.equals(timeStamp, that.timeStamp) &&
            state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUsername, toUsername, text, timeStamp, state);
    }
}
