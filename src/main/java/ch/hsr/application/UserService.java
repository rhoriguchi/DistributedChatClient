package ch.hsr.application;

import ch.hsr.application.exception.FriendException;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.domain.friend.FriendState;
import ch.hsr.domain.peer.Peer;
import ch.hsr.mapping.friend.FriendRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    public void sendFriendRequest(Username username) {
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
    }

    public void sendAcceptFriendRequest(Username username) {
        sendUpdateFriendRequest(username, FriendState.ACCEPTED);
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
            friendRepository.createFriend(friend);
        }
    }

    public void sendRejectFriendRequest(Username username) {
        sendUpdateFriendRequest(username, FriendState.REJECTED);
    }

    public Stream<Friend> getAllOpenFriendRequests() {
        return friendRepository.getAllFriendsWithState(FriendState.RECEIVED);
    }
}
