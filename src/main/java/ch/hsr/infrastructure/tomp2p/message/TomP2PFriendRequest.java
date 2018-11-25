package ch.hsr.infrastructure.tomp2p.message;

import lombok.Data;
import java.io.Serializable;

@Data
public class TomP2PFriendRequest implements Serializable {

    private static final long serialVersionUID = 6806756251563409176L;

    private final String fromUsername;
    private final String state;
    private final boolean failed;

}
