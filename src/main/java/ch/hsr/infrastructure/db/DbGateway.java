package ch.hsr.infrastructure.db;

import java.util.stream.Stream;

public interface DbGateway {

    void createFriend(DbFriend dbFriend);

    Stream<DbFriend> getAllFriends(String ownerUsername);

    DbMessage createMessage(DbMessage dbMessage);

    Stream<DbMessage> getAllMessages(String ownerUsername, String otherUsername);

    // TODO change this to id
    void deleteMessage(DbMessage dbMessage);
}
