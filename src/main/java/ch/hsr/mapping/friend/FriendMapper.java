package ch.hsr.mapping.friend;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.infrastructure.db.DbFriend;
import ch.hsr.infrastructure.db.DbGateway;
import java.util.stream.Stream;

public class FriendMapper implements FriendRepository {

    private final DbGateway dbGateway;

    public FriendMapper(DbGateway dbGateway) {
        this.dbGateway = dbGateway;
    }

    @Override
    public void create(Friend friend) {
        dbGateway.createFriend(
            friend.getUsername().toString(),
            friend.getOwnerUsername().toString()
        );
    }

    private Friend dbFriendToFriend(DbFriend dbFriend) {
        return new Friend(
            Username.fromString(dbFriend.getUsername()),
            Username.fromString(dbFriend.getOwnerUsername())
        );
    }

    @Override
    public Stream<Friend> getAll(Username ownerUsername) {
        return dbGateway.getAllFriends(ownerUsername.toString())
            .map(this::dbFriendToFriend);
    }

}
