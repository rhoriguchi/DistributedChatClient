package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PMessage extends DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = 4261139652482734858L;

    public TomP2PMessage(Long id,
                         String fromUsername,
                         String toUsername,
                         String text,
                         String timeStamp,
                         TomP2PMessageState state) {
        super(id, fromUsername, toUsername, text, timeStamp, state);
    }
}
