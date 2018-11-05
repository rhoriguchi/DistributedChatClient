package ch.hsr.mapping.friend;

import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.PeerId;
import ch.hsr.domain.peer.Username;
import ch.hsr.infrastructure.db.DbFriend;
import ch.hsr.infrastructure.db.DbGateway;
import java.util.stream.Stream;

public class FriendMapper implements FriendRepository {

    private final DbGateway dbGateway;

    public FriendMapper(DbGateway dbGateway) {
        this.dbGateway = dbGateway;
    }

    @Override
    public Peer create(Peer peer) {
        DbFriend dbFriend = dbGateway.createFriend(peerToDbFriend(peer));
        return dbFriendsToPeer(dbFriend);
    }

    private DbFriend peerToDbFriend(Peer peer) {
        return new DbFriend(
            peer.getPeerId().toString(),
            peer.getUsername().toString()
        );
    }

    private Peer dbFriendsToPeer(DbFriend dbFriend) {
        return new Peer(
            PeerId.fromString(dbFriend.getId()),
            Username.fromString(dbFriend.getUsername()),
            // TODO needs to get fetched from TomP2P interface
            IpAddress.empty()
        );
    }

    @Override
    public Stream<Peer> getAll() {
        return dbGateway.getAllFriends()
            .map(this::dbFriendsToPeer);
    }

}
