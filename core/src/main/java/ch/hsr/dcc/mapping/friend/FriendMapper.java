package ch.hsr.dcc.mapping.friend;

import ch.hsr.dcc.application.exception.FriendException;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.friend.Friend;
import ch.hsr.dcc.domain.friend.FriendState;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.infrastructure.db.DbFriend;
import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dcc.mapping.Util.TomP2PPeerAddressHelper;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;
import java.util.stream.Stream;

public class FriendMapper implements FriendRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendMapper.class);

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    private final PeerRepository peerRepository;

    public FriendMapper(DbGateway dbGateway,
                        TomP2P tomP2P,
                        PeerRepository peerRepository) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
        this.peerRepository = peerRepository;
    }

    @Override
    public void send(Friend friend) {
        Peer peer = peerRepository.get(friend.getFriend().getUsername());

        if (peer.isOnline()) {
            DbFriend dbFriend = dbGateway.saveFriend(
                DbFriend.newDbFriend(
                    friend.getFriend().getUsername().toString(),
                    friend.getSelf().getUsername().toString(),
                    friend.getState().name(),
                    friend.isFailed()
                )
            );

            try {
                tomP2P.sendFriendRequest(dbFriendToTomP2PFriendRequest(dbFriend),
                    TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);

                dbGateway.getFriend(dbFriend.getUsername(), dbFriend.getOwnerUsername())
                    .ifPresent(dbFriend1 -> {
                        dbFriend1.setFailed(true);
                        dbGateway.saveFriend(dbFriend1);
                    });
            }
        } else {
            throw new FriendException(String.format("Peer %s is offline", peer.getUsername()));
        }
    }

    private TomP2PFriendRequest dbFriendToTomP2PFriendRequest(DbFriend dbFriend) {
        return new TomP2PFriendRequest(
            dbFriend.getUsername(),
            dbFriend.getState(),
            dbFriend.isFailed()
        );
    }

    @Override
    public void saveFriend(Friend friend) {
        dbGateway.saveFriend(friendToDbFriend(friend));
    }

    private DbFriend friendToDbFriend(Friend friend) {
        return new DbFriend(
            friend.getFriend().getUsername().toString(),
            friend.getSelf().getUsername().toString(),
            friend.getState().name(),
            friend.isFailed()
        );
    }

    @Override
    public Stream<Friend> getAll(Username ownerUsername) {
        return dbGateway.getAllFriends(ownerUsername.toString())
            .map(this::dbFriendToFriend);
    }

    private Friend dbFriendToFriend(DbFriend dbFriend) {
        return new Friend(
            peerRepository.get(Username.fromString(dbFriend.getUsername())),
            peerRepository.get(Username.fromString(dbFriend.getOwnerUsername())),
            dbFriend.isFailed(),
            FriendState.valueOf(dbFriend.getState())
        );
    }

    @Override
    public Stream<Friend> getAllFriendsWithState(FriendState friendState) {
        return dbGateway.getAllFriendsWithState(friendState.name())
            .map(this::dbFriendToFriend);
    }

    @Override
    public Friend getOldestReceivedFriendRequest() {
        return tomP2PFriendRequestToFriend(tomP2P.getOldestReceivedTomP2PFriendRequest());
    }

    private Friend tomP2PFriendRequestToFriend(TomP2PFriendRequest tomP2PFriendRequest) {
        return new Friend(
            peerRepository.get(Username.fromString(tomP2PFriendRequest.getFromUsername())),
            peerRepository.getSelf(),
            tomP2PFriendRequest.isFailed(),
            FriendState.valueOf(tomP2PFriendRequest.getState())
        );
    }

    @Override
    public Optional<Friend> getFriend(Username username) {
        Peer self = peerRepository.getSelf();

        return dbGateway.getFriend(username.toString(), self.getUsername().toString())
            .map(this::dbFriendToFriend);
    }

}
