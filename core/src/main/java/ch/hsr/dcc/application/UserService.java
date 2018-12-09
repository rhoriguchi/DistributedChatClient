package ch.hsr.dcc.application;

import ch.hsr.dcc.application.exception.FriendException;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.friend.Friend;
import ch.hsr.dcc.domain.friend.FriendState;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.mapping.friend.FriendRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import java.util.Optional;
import java.util.stream.Stream;

//TODO once rejected user can't be added as friend
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final FriendRepository friendRepository;
    private final PeerRepository peerRepository;

    public UserService(FriendRepository friendRepository, PeerRepository peerRepository) {
        this.friendRepository = friendRepository;
        this.peerRepository = peerRepository;
    }

    @Async
    public void sendFriendRequest(Username username) {
        if (!username.isEmpty()) {
            Peer self = peerRepository.getSelf();

            if (!self.getUsername().equals(username)) {
                Peer friend = peerRepository.get(username);

                Optional<Friend> optionalFriend = friendRepository.getFriend(username);
                if (optionalFriend.isPresent()) {
                    Friend localFriend = optionalFriend.get();

                    switch (localFriend.getState()) {
                        case ACCEPTED:
                            throw new FriendException(String.format("You are already friends with %s", username));
                        case REJECTED:
                            friendRepository.send(Friend.newFriend(friend, self));
                            break;
                        case SENT:
                            throw new FriendException(String.format("You have already friends sent a request to %s",
                                username));
                        case RECEIVED:
                            sendAcceptFriendRequest(username);
                    }
                } else {
                    friendRepository.send(Friend.newFriend(friend, self));
                }
            } else {
                throw new FriendException("You can't add yourself as friend");
            }
        } else {
            throw new FriendException("Can't send friend request to empty username");
        }
    }

    @Async
    public void sendAcceptFriendRequest(Username username) {
        if (!username.isEmpty()) {
            sendUpdateFriendRequest(username, FriendState.ACCEPTED);
        } else {
            throw new FriendException("Can't accept friend request of empty username");
        }
    }

    private void sendUpdateFriendRequest(Username username, FriendState friendState) {
        Optional<Friend> optionalFriend = friendRepository.getFriend(username);
        if (optionalFriend.isPresent()) {
            Friend friend = optionalFriend.get();
            friend.setState(friendState);

            friendRepository.send(friend);
        } else {
            throw new FriendException("You don't have a friend request from this user");
        }
    }

    public Stream<Friend> getAllFriends() {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        return friendRepository.getAll(ownerUsername);
    }

    @Async
    //TODO check signature
    public void friendRequestReceived() {
        Friend friend = friendRepository.getOldestReceivedFriendRequest();

        Optional<Friend> optionalFriend = friendRepository.getFriend(friend.getFriend().getUsername());
        if (optionalFriend.isPresent()) {
            Friend localFriend = optionalFriend.get();

            switch (localFriend.getState()) {
                case ACCEPTED:
                    sendAcceptFriendRequest(friend.getFriend().getUsername());
                    break;
                case REJECTED:
                    sendRejectFriendRequest(friend.getFriend().getUsername());
                    break;
                case SENT:
                    sendAcceptFriendRequest(friend.getFriend().getUsername());
                    break;
                case RECEIVED:
                    LOGGER.info(String.format("Friend request received again from %s",
                        friend.getFriend().getUsername()));
            }
        } else {
            friend.setState(FriendState.RECEIVED);
            friendRepository.saveFriend(friend);
        }
    }

    @Async
    public void sendRejectFriendRequest(Username username) {
        if (!username.isEmpty()) {
            sendUpdateFriendRequest(username, FriendState.REJECTED);
        } else {
            throw new FriendException("Can't reject friend request of empty username");
        }
    }

    public Stream<Friend> getAllOpenFriendRequests() {
        return friendRepository.getAllFriendsWithState(FriendState.RECEIVED);
    }
}
