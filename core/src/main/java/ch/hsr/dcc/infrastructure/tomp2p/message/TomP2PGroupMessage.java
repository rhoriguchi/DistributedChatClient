package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString (callSuper = true)
@EqualsAndHashCode (callSuper = true)
public class TomP2PGroupMessage extends TomP2PMessage {

    private static final long serialVersionUID = 3102528355991144733L;

    private final Long toGroupId;

    public TomP2PGroupMessage(Long toGroupId,
                              String fromUsername,
                              String toUsername,
                              String text,
                              String timeStamp,
                              String signature) {
        super(fromUsername, toUsername, text, timeStamp, signature);
        this.toGroupId = toGroupId;
    }
}