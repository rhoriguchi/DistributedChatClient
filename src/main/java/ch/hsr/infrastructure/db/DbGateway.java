package ch.hsr.infrastructure.db;

import ch.hsr.domain.common.PeerId;
import java.util.stream.Stream;

public interface DbGateway {

    DbFriend createFriend(DbFriend dbFriend);

    Stream<DbFriend> getAllFriends(String ownerId);
}
