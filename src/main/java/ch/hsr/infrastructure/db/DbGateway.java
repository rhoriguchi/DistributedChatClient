package ch.hsr.infrastructure.db;

import java.util.stream.Stream;

public interface DbGateway {

    DbFriend createFriend(DbFriend dbFriend);

    Stream<DbFriend> getAllFriends(String ownerId);

    DbMessage createMessage(DbMessage dbMessage);

    Stream<DbMessage> getAllMessages(String ownerId, String otherId);
}
