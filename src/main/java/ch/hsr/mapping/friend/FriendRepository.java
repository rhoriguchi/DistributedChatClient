package ch.hsr.mapping.friend;

import ch.hsr.domain.peer.Peer;
import java.util.stream.Stream;

public interface FriendRepository {

    Peer create(Peer peer);

    Stream<Peer> getAll();

}
