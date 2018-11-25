package ch.hsr.infrastructure.db;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

//TODO use db object instead of many variables
//TODO use for updated and create same method
public interface DbGateway {

    void createFriend(String username, String ownerUsername);

    Stream<DbFriend> getAllFriends(String ownerUsername);

    void createGroup(String name, Collection<String> members);

    Optional<DbGroup> getGroup(Long groupId);

    Stream<DbGroup> getAllGroups(String username);

    DbMessage saveMessage(DbMessage dbMessage);

    Stream<DbMessage> getAllMessages(String ownerUsername, String otherUsername);

    Optional<DbMessage> getMessage(Long id);

    DbGroupMessage saveGroupMessage(DbGroupMessage dbGroupMessage);

    Stream<DbGroupMessage> getAllGroupMessages(Long toGroupId);

    Optional<DbGroupMessage> getGroupMessage(Long id);

    Optional<DbKeyPair> getKeyPair(String username);

    DbKeyPair createKeyPair(String username, String privateKey, String publicKey);
}
