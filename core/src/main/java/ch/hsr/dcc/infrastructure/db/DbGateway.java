package ch.hsr.dcc.infrastructure.db;

import java.util.Optional;
import java.util.stream.Stream;

public interface DbGateway {

    DbFriend saveFriend(DbFriend dbFriend);

    Stream<DbFriend> getAllFriends(String ownerUsername);

    Stream<DbFriend> getAllFriendsWithState(String state);

    Optional<DbFriend> getFriend(String username, String ownerUsername);

    DbGroup saveGroup(DbGroup dbGroup);

    Optional<DbGroup> getGroup(Long groupId);

    Stream<DbGroup> getAllGroups(String username);

    DbMessage saveMessage(DbMessage dbMessage);

    Stream<DbMessage> getAllMessages(String ownerUsername, String otherUsername);

    Optional<DbMessage> getMessage(Long id);

    DbGroupMessage saveGroupMessage(DbGroupMessage dbGroupMessage);

    Stream<DbGroupMessage> getAllGroupMessages(Long toGroupId);

    Optional<DbGroupMessage> getGroupMessage(Long id);

    Optional<DbKeyPair> getKeyPair(String username);

    DbKeyPair saveKeyPair(DbKeyPair dbKeyPair);
}
