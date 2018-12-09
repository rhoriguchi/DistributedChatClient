package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.Data;
import java.io.Serializable;

@Data
public class TomP2PFriendRequest implements Serializable {

    private static final long serialVersionUID = 1482751596279870623L;

    private final String fromUsername;
    private final String state;
    private final String signature;
    private final boolean failed;

}
