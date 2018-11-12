package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PGroupMessage extends TomP2PMessage implements Serializable {

    private static final long serialVersionUID = -8455168767732688742L;

    private final Long toGroupId;

    public TomP2PGroupMessage(Long id,
                              String fromUsername,
                              String toUsername,
                              String text,
                              String timeStamp,
                              TomP2PMessageState state,
                              Long toGroupId) {
        super(id, fromUsername, toUsername, text, timeStamp, state);
        this.toGroupId = toGroupId;
    }
}
