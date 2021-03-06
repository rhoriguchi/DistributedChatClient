package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.Data;
import lombok.ToString;
import java.util.Objects;

@Data
@ToString (callSuper = true)
public class TomP2PGroupMessage extends TomP2PMessage {

    private static final long serialVersionUID = 8578810371946702243L;

    private final Long toGroupId;

    public TomP2PGroupMessage(Long groupId,
                              String fromUsername,
                              String toUsername,
                              String text,
                              String timeStamp,
                              String signature) {
        super(fromUsername, toUsername, text, timeStamp, signature);
        this.toGroupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TomP2PGroupMessage that = (TomP2PGroupMessage) o;
        return Objects.equals(toGroupId, that.toGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toGroupId);
    }
}
