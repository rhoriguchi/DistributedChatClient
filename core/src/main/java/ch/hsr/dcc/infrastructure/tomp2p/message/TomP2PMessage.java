package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
@AllArgsConstructor
public class TomP2PMessage implements Serializable {

    private static final long serialVersionUID = 975854367077058938L;

    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String timeStamp;
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
        return Objects.equals(fromUsername, that.fromUsername) &&
            Objects.equals(toUsername, that.toUsername) &&
            Objects.equals(text, that.text) &&
            Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUsername, toUsername, text, timeStamp);
    }
}
