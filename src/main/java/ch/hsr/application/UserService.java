package ch.hsr.application;

import ch.hsr.application.exception.FriendException;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.domain.peer.Peer;
import ch.hsr.mapping.friend.FriendRepository;
import ch.hsr.mapping.peer.PeerRepository;
import java.util.stream.Stream;

public class UserService {

    private final FriendRepository friendRepository;
    private final PeerRepository peerRepository;

    public UserService(FriendRepository friendRepository, PeerRepository peerRepository) {
        this.friendRepository = friendRepository;
        this.peerRepository = peerRepository;
    }

    public void addFriend(Username username) {
        Peer self = peerRepository.getSelf();

        if (!self.getUsername().equals(username)) {
            Peer friend = peerRepository.get(username);

            friendRepository.create(new Friend(self, friend));
        } else {
            throw new FriendException("You can't add yourself as friend");
        }
    }

    public Stream<Friend> getAllFriends() {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        return friendRepository.getAll(ownerUsername);
    }
}
