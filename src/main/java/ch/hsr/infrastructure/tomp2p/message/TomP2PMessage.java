package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PMessage extends DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = 7942807783128373047L;

    private final String state;

    public TomP2PMessage(Long id,
                         String fromUsername,
                         String toUsername,
                         String text,
                         String timeStamp,
                         String signature,
                         String state) {
        super(id, fromUsername, toUsername, text, timeStamp, signature);
        this.state = state;
    }
}
