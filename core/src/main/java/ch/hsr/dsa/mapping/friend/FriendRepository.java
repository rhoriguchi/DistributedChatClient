package ch.hsr.dsa.mapping.friend;

import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.friend.Friend;
import ch.hsr.dsa.domain.friend.FriendState;
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
