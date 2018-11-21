package ch.hsr.infrastructure.db;

import ch.hsr.infrastructure.db.specification.DbGroupSpecification;
import ch.hsr.infrastructure.db.specification.DbMessageSpecification;
import java.util.Collection;
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
    public DbMessage saveMessage(DbMessage dbMessage) {
        return dbMessageRepository.save(dbMessage);
    }

    @Override
    public Stream<DbMessage> getAllMessages(String ownerUsername, String otherUsername) {
        return iterableToStream(dbMessageRepository.findAll(
            DbMessageSpecification.messageHasFromUsernameOrToUsername(ownerUsername, otherUsername)));
    }

    @Override
    public Optional<DbMessage> getMessage(Long id) {
        return dbMessageRepository.findById(id);
    }

    @Override
    public DbGroupMessage saveGroupMessage(DbGroupMessage dbGroupMessage) {
        return dbGroupMessageRepository.save(dbGroupMessage);
    }

    @Override
    public Stream<DbGroupMessage> getAllGroupMessages(Long toGroupId) {
        return iterableToStream(dbGroupMessageRepository.findByGroupId(toGroupId));
    }

    @Override
    public Optional<DbGroupMessage> getGroupMessage(Long id) {
        return dbGroupMessageRepository.findById(id);
    }

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
