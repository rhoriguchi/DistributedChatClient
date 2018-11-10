package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PMessage extends TomP2PDefaultMessage implements Serializable {

    private static final long serialVersionUID = 2740950040116158893L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String messageTimeStamp;
    private boolean received;

    public TomP2PMessage(TomP2PDefaultMessageState state,
                         Long id,
                         String fromUsername,
                         String toUsername,
                         String text,
                         String messageTimeStamp,
                         boolean received) {
        super(state);
        this.id = id;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.text = text;
        this.messageTimeStamp = messageTimeStamp;
        this.received = received;
    }
}
