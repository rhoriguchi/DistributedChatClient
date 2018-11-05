package ch.hsr.infrastructure.db;

import java.util.stream.Stream;

public interface DbGateway {

    DbFriend createFriend(DbFriend dbFriend);

    Stream<DbFriend> getAllFriends();
}
