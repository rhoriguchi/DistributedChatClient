package ch.hsr.infrastructure.tomp2p.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class DefaultTomP2PMessage implements Serializable {

    private static final long serialVersionUID = -5027658218161995044L;

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String timeStamp;
    private TomP2PMessageState state;
}
