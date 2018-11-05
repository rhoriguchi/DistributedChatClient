package ch.hsr.application;

import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.PeerId;
import ch.hsr.domain.peer.Username;
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

    public Peer addFriend(Username username) {
        PeerId peerId = peerRepository.getPeerId(username);
        return friendRepository.create(Peer.newPeer(peerId, username));
    }

    public Stream<Peer> getAllFriends() {
        return friendRepository.getAll();
    }
}
