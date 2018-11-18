package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PGroupMessage extends DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = -7261273768644475932L;

    private final Long toGroupId;

    public TomP2PGroupMessage(Long id,
                              String fromUsername,
                              String toUsername,
                              Long toGroupId,
                              String text,
                              String timeStamp,
                              String signature,
                              TomP2PMessageState state) {
        super(id, fromUsername, toUsername, text, timeStamp, signature, state);
        this.toGroupId = toGroupId;
    }
}
