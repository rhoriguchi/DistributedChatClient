package ch.hsr.mapping.friend;

import ch.hsr.domain.common.PeerId;
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
    public Friend create(Friend friend) {
        DbFriend dbFriend = dbGateway.createFriend(friendToDbFriend(friend));
        return dbFriendToFriend(dbFriend);
    }

    private DbFriend friendToDbFriend(Friend friend) {
        return new DbFriend(
            friend.getPeerId().toString(),
            friend.getUsername().toString(),
            friend.getOwnerId().toString()
        );
    }

    private Friend dbFriendToFriend(DbFriend dbFriend) {
        return new Friend(
            PeerId.fromString(dbFriend.getId()),
            Username.fromString(dbFriend.getUsername()),
            PeerId.fromString(dbFriend.getOwnerId())
        );
    }

    @Override
    public Stream<Friend> getAll() {
        return dbGateway.getAllFriends()
            .map(this::dbFriendToFriend);
    }

}
