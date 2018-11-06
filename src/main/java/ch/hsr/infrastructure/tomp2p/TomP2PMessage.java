package ch.hsr.infrastructure.tomp2p;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public class TomP2PMessage implements Serializable {

    private final Long id;
    private final String fromUsername;
    private final String toUsername;
    private final String text;
    private final String messageTimeStamp;
    private Boolean received;
}
