package ch.hsr.dcc.mapping.friend;

import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.friend.Friend;
import ch.hsr.dcc.domain.friend.FriendState;
import java.util.Optional;
import java.util.stream.Stream;

public interface FriendRepository {

    void send(Friend friend);

    void createFriend(Friend friend);

    Stream<Friend> getAll(Username ownerUsername);

    Stream<Friend> getAllFriendsWithState(FriendState friendState);

    Friend getOldestReceivedFriendRequest();

    Optional<Friend> getFriend(Username username);
}
