package ch.hsr.mapping.friend;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import java.util.stream.Stream;

public interface FriendRepository {

    Friend create(Friend friend);

    Stream<Friend> getAll(Username ownerUsername);

}
