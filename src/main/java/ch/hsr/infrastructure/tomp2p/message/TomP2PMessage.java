package ch.hsr.infrastructure.tomp2p.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class TomP2PMessage implements Serializable {

    private static final long serialVersionUID = -4280740592279143466L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String timeStamp;
    private final String signature;
    private boolean failed;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TomP2PMessage that = (TomP2PMessage) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(fromUsername, that.fromUsername) &&
            Objects.equals(toUsername, that.toUsername) &&
            Objects.equals(text, that.text) &&
            Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUsername, toUsername, text, timeStamp);
    }
}
