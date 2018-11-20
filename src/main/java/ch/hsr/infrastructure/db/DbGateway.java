package ch.hsr.infrastructure.db;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface DbGateway {

    void createFriend(String username, String ownerUsername);

    Stream<DbFriend> getAllFriends(String ownerUsername);

    void createGroup(String name, Collection<String> members);

    Optional<DbGroup> getGroup(Long groupId);

    Stream<DbGroup> getAllGroups(String username);

    DbMessage createMessage(String fromUsername, String toUsername, String text, String timeStamp, String state, String signState);

    DbMessage updateMessage(Long id, String fromUsername, String toUsername, String text, String timeStamp, String state, String signState);

    Stream<DbMessage> getAllMessages(String ownerUsername, String otherUsername);

    Optional<DbMessage> getMessage(Long id);

    void deleteMessage(Long id);

    DbGroupMessage createGroupMessage(String fromUsername, Long toGroupId, String text, String timeStamp, Map<String, String> states, String signState);

    DbGroupMessage updateGroupMessage(Long id, String fromUsername, Long toGroupId, String text, String timeStamp, Map<String, String> states, String signState);

    Stream<DbGroupMessage> getAllGroupMessages(Long toGroupId);

    Optional<DbGroupMessage> getGroupMessage(Long id);

    void deleteGroupMessage(Long id);

    Optional<DbKeyPair> getKeyPair(String username);

    DbKeyPair createKeyPair(String username, String privateKey, String publicKey);
}
