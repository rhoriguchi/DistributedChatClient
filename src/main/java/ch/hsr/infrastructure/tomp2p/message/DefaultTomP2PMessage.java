package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import java.io.Serializable;
import java.util.Objects;

@Data
public class DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = 5878089601369683472L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String timeStamp;
    private final String signature;

    public DefaultTomP2PMessage(Long id,
                                String fromUsername,
                                String toUsername,
                                String text,
                                String timeStamp,
                                String signature) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.text = text;
        this.timeStamp = timeStamp;
        this.signature = signature;
    }

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
            Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fromUsername, toUsername, text, timeStamp);
    }
}
