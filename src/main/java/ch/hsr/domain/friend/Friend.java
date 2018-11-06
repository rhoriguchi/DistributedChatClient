package ch.hsr.domain.friend;

import ch.hsr.domain.common.PeerId;
import ch.hsr.domain.common.Username;
import lombok.Data;

@Data
public class Friend {

    private final PeerId peerId;
    private final Username username;
    private final PeerId ownerId;
}
