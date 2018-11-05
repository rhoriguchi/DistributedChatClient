package ch.hsr.domain.peer;

import ch.hsr.domain.common.PeerId;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode (callSuper = true)
@ToString (callSuper = true)
public class Peer extends Friend {

    private final IpAddress ipAddress;

    public Peer(PeerId peerId, Username username, IpAddress ipAddress) {
        super(peerId, username);
        this.ipAddress = ipAddress;
    }
}
