package ch.hsr.dcc.domain.friend;

import ch.hsr.dcc.domain.peer.Peer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Friend {

    private final Peer friend;
    private final Peer self;
    private final boolean failed;
    private FriendState state;

    public static Friend newFriend(Peer friend, Peer self) {
        return new Friend(
            friend,
            self,
            false,
            FriendState.SENT
        );
    }
}
