package ch.hsr.mapping.friend;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.infrastructure.db.DbFriend;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.mapping.peer.PeerRepository;
import java.util.stream.Stream;

public class FriendMapper implements FriendRepository {

    private final DbGateway dbGateway;

    private final PeerRepository peerRepository;

    public FriendMapper(DbGateway dbGateway, PeerRepository peerRepository) {
        this.dbGateway = dbGateway;
        this.peerRepository = peerRepository;
    }

    @Override
    public void create(Friend friend) {
        dbGateway.createFriend(
            friend.getFriend().getUsername().toString(),
            friend.getSelf().getUsername().toString()
        );
    }

    private Friend dbFriendToFriend(DbFriend dbFriend) {
        return new Friend(
            peerRepository.get(Username.fromString(dbFriend.getUsername())),
            peerRepository.get(Username.fromString(dbFriend.getOwnerUsername()))
        );
    }

    @Override
    public Stream<Friend> getAll(Username ownerUsername) {
        return dbGateway.getAllFriends(ownerUsername.toString())
            .map(this::dbFriendToFriend);
    }

}
