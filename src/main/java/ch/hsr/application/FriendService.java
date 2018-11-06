package ch.hsr.application;

import ch.hsr.domain.common.PeerId;
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

    public Friend addFriend(Username username) {
        PeerId ownerId = peerRepository.getSelf().getPeerId();
        PeerId peerId = peerRepository.getPeerId(username);

        if (!ownerId.equals(peerId)) {
            return friendRepository.create(new Friend(peerId, username, ownerId));
        } else {
            throw new IllegalArgumentException("You can't add yourself as friend");
        }
    }

    public Stream<Friend> getAllFriends() {
        PeerId ownerId = peerRepository.getSelf().getPeerId();

        return friendRepository.getAll(ownerId);
    }
}
