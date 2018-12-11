package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class TomP2PMessage implements Serializable {

    private static final long serialVersionUID = -487541519599716472L;

    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String timeStamp;
}
