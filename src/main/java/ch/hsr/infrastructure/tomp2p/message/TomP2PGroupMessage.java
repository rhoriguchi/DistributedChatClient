package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.io.Serializable;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class TomP2PGroupMessage extends DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = -6992360540836476006L;

    private final Long toGroupId;

    public TomP2PGroupMessage(Long id,
                              String fromUsername,
                              String toUsername,
                              Long toGroupId,
                              String text,
                              String timeStamp,
                              TomP2PMessageState state) {
        super(id, fromUsername, toUsername, text, timeStamp, state);
        this.toGroupId = toGroupId;
    }
}
