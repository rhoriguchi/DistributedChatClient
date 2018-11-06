package ch.hsr.application;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.friend.Friend;
import ch.hsr.mapping.friend.FriendRepository;
import ch.hsr.mapping.peer.PeerRepository;
import java.util.stream.Stream;

public class FriendService {

    private final FriendRepository friendRepository;
    private final PeerRepository peerRepository;

    public FriendService(FriendRepository friendRepository, PeerRepository peerRepository) {
        this.friendRepository = friendRepository;
        this.peerRepository = peerRepository;
    }

    public void addFriend(Username username) {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        if (!ownerUsername.equals(username)) {
            friendRepository.create(new Friend(username, ownerUsername));
        } else {
            throw new IllegalArgumentException("You can't add yourself as friend");
        }
    }

    public Stream<Friend> getAllFriends() {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        return friendRepository.getAll(ownerUsername);
    }
}
