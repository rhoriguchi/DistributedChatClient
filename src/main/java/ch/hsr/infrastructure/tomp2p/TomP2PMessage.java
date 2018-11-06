package ch.hsr.infrastructure.tomp2p;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class TomP2PMessage implements Serializable {

    private static final long serialVersionUID = 2740950040116158893L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String messageTimeStamp;
    private boolean received;
}
