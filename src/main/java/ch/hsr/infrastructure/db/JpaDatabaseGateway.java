package ch.hsr.infrastructure.db;

import ch.hsr.infrastructure.db.specification.DbGroupSpecification;
import ch.hsr.infrastructure.db.specification.DbMessageSpecification;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JpaDatabaseGateway implements DbGateway {

    private final DbFriendRepository dbFriendRepository;
    private final DbGroupRepository dbGroupRepository;
    private final DbMessageRepository dbMessageRepository;
    private final DbGroupMessageRepository dbGroupMessageRepository;
    private final DbKeyStoreRepository dbKeyStoreRepository;

    public JpaDatabaseGateway(DbFriendRepository dbFriendRepository,
                              DbGroupRepository dbGroupRepository,
                              DbMessageRepository dbMessageRepository,
                              DbGroupMessageRepository dbGroupMessageRepository,
                              DbKeyStoreRepository dbKeyStoreRepository) {
        this.dbFriendRepository = dbFriendRepository;
        this.dbGroupRepository = dbGroupRepository;
        this.dbMessageRepository = dbMessageRepository;
        this.dbGroupMessageRepository = dbGroupMessageRepository;
        this.dbKeyStoreRepository = dbKeyStoreRepository;
    }

    @Override
    public void createFriend(String username, String ownerUsername) {
        dbFriendRepository.save(new DbFriend(username, ownerUsername));
    }

    @Override
    public Stream<DbFriend> getAllFriends(String ownerUsername) {
        return iterableToStream(dbFriendRepository.findByOwnerUsername(ownerUsername));
    }

    private <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public void createGroup(String name, Collection<String> members) {
        dbGroupRepository.save(new DbGroup(name, members));
    }

    @Override
    public Optional<DbGroup> getGroup(Long groupId) {
        return dbGroupRepository.findById(groupId);
    }

    @Override
    public Stream<DbGroup> getAllGroups(String username) {
        return iterableToStream(dbGroupRepository.findAll(
            DbGroupSpecification.groupContainsMember(username)));
    }

    @Override
    public DbMessage createMessage(String fromUsername,
                                   String toUsername,
                                   String text,
                                   String timeStamp,
                                   boolean receive,
                                   boolean valid) {
        return dbMessageRepository.save(DbMessage.newDbMessage(
            fromUsername,
            toUsername,
            text,
            timeStamp,
            receive,
            valid
        ));
    }

    @Override
    public DbMessage updateMessage(Long id,
                                   String fromUsername,
                                   String toUsername,
                                   String text,
                                   String timeStamp,
                                   boolean receive,
                                   boolean valid) {
        return dbMessageRepository.save(new DbMessage(
            id,
            fromUsername,
            toUsername,
            text,
            timeStamp,
            receive,
            valid
        ));
    }

    @Override
    public Stream<DbMessage> getAllMessages(String ownerUsername, String otherUsername) {
        return iterableToStream(dbMessageRepository.findAll(
            DbMessageSpecification.messageHasFromUsernameOrToUsername(ownerUsername, otherUsername)));
    }

    @Override
    public void deleteMessage(DbMessage dbMessage) {
        dbMessageRepository.delete(dbMessage);
    }

    @Override
    public DbGroupMessage createGroupMessage(String fromUsername,
                                             Long toGroupId,
                                             String text,
                                             String timeStamp,
                                             Map<String, Boolean> received,
                                             boolean valid) {
        return dbGroupMessageRepository.save(DbGroupMessage.newDbGroupMessage(
            fromUsername,
            toGroupId,
            text,
            timeStamp,
            received,
            valid
        ));
    }

    @Override
    public DbGroupMessage updateGroupMessage(Long id,
                                             String fromUsername,
                                             Long toGroupId,
                                             String text,
                                             String timeStamp,
                                             Map<String, Boolean> received,
                                             boolean valid) {
        return dbGroupMessageRepository.save(new DbGroupMessage(
            id,
            fromUsername,
            toGroupId,
            text,
            timeStamp,
            received,
            valid
        ));
    }

    @Override
    public Stream<DbGroupMessage> getAllGroupMessages(Long toGroupId) {
        return iterableToStream(dbGroupMessageRepository.findByToGroupId(toGroupId));
    }

    @Override
    public Optional<DbKeyPair> getKeyPair(String username) {
        return dbKeyStoreRepository.findById(username);
    }

    @Override
    public DbKeyPair createKeyPair(String username, String privateKey, String publicKey) {
        return dbKeyStoreRepository.save(new DbKeyPair(
            username,
            privateKey,
            publicKey
        ));
    }
}
