package ch.hsr.dcc.infrastructure.db;

import ch.hsr.dcc.event.dbchanged.DbSavedEventPublisher;
import ch.hsr.dcc.infrastructure.db.specification.DbFriendSpecification;
import ch.hsr.dcc.infrastructure.db.specification.DbGroupSpecification;
import ch.hsr.dcc.infrastructure.db.specification.DbMessageSpecification;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JpaDatabaseGateway implements DbGateway {

    private final DbSavedEventPublisher dbSavedEventPublisher;

    private final DbFriendRepository dbFriendRepository;
    private final DbGroupRepository dbGroupRepository;
    private final DbMessageRepository dbMessageRepository;
    private final DbGroupMessageRepository dbGroupMessageRepository;

    public JpaDatabaseGateway(DbSavedEventPublisher dbSavedEventPublisher,
                              DbFriendRepository dbFriendRepository,
                              DbGroupRepository dbGroupRepository,
                              DbMessageRepository dbMessageRepository,
                              DbGroupMessageRepository dbGroupMessageRepository) {
        this.dbSavedEventPublisher = dbSavedEventPublisher;
        this.dbFriendRepository = dbFriendRepository;
        this.dbGroupRepository = dbGroupRepository;
        this.dbMessageRepository = dbMessageRepository;
        this.dbGroupMessageRepository = dbGroupMessageRepository;
    }

    @Override
    public DbFriend saveFriend(DbFriend dbFriend) {
        DbFriend newDbFriend = dbFriendRepository.save(dbFriend);
        dbSavedEventPublisher.dbFriendChanged(newDbFriend.getUsername());
        return newDbFriend;
    }

    @Override
    public Stream<DbFriend> getAllFriends(String ownerUsername) {
        return iterableToStream(dbFriendRepository.findByOwnerUsername(ownerUsername));
    }

    private <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public Stream<DbFriend> getAllFriendsWithState(String state) {
        return iterableToStream(dbFriendRepository.findByState(state));
    }

    @Override
    public Optional<DbFriend> getFriend(String username, String ownerUsername) {
        return dbFriendRepository.findOne(DbFriendSpecification.getFriend(username, ownerUsername));
    }

    @Override
    public DbGroup saveGroup(DbGroup dbGroup) {
        DbGroup newDbGroup = dbGroupRepository.save(dbGroup);
        dbSavedEventPublisher.dbGroupChanged(newDbGroup.getId());
        return newDbGroup;
    }

    @Override
    public void deleteGroup(Long groupId) {
        dbGroupMessageRepository.deleteById(groupId);
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
        DbMessage newDbMessage = dbMessageRepository.save(dbMessage);
        dbSavedEventPublisher.dbMessageEventChanged(newDbMessage.getId());
        return newDbMessage;
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
        DbGroupMessage newDbGroupMessage = dbGroupMessageRepository.save(dbGroupMessage);
        dbSavedEventPublisher.dbGroupMessageEventChanged(newDbGroupMessage.getId());
        return newDbGroupMessage;
    }

    @Override
    public Stream<DbGroupMessage> getAllGroupMessages(Long toGroupId) {
        return iterableToStream(dbGroupMessageRepository.findByGroupId(toGroupId));
    }

    @Override
    public Optional<DbGroupMessage> getGroupMessage(Long id) {
        return dbGroupMessageRepository.findById(id);
    }
}
