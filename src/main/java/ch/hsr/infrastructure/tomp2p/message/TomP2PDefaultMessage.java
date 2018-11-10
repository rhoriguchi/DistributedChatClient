package ch.hsr.infrastructure.tomp2p.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

@Data
@AllArgsConstructor
public abstract class TomP2PDefaultMessage implements Serializable {

    private static final long serialVersionUID = -6372868657686939229L;

    private TomP2PDefaultMessageState state;
}
