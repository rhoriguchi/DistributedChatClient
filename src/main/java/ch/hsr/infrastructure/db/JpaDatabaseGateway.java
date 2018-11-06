package ch.hsr.infrastructure.db;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JpaDatabaseGateway implements DbGateway {

    private final DbFriendRepository dbFriendRepository;
    private final DbMessageRepository dbMessageRepository;

    public JpaDatabaseGateway(DbFriendRepository dbFriendRepository, DbMessageRepository dbMessageRepository) {
        this.dbFriendRepository = dbFriendRepository;
        this.dbMessageRepository = dbMessageRepository;
    }

    @Override
    public DbFriend createFriend(DbFriend dbFriend) {
        return dbFriendRepository.save(dbFriend);
    }

    @Override
    public Stream<DbFriend> getAllFriends(String ownerId) {
        return iterableToStream(dbFriendRepository.findByOwnerId(ownerId));
    }

    private <T> Stream<T> iterableToStream(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    @Override
    public DbMessage createMessage(DbMessage dbMessage) {
        return dbMessageRepository.save(dbMessage);
    }

    @Override
    public Stream<DbMessage> getAllMessages(String ownerId, String otherId) {
        return iterableToStream(dbMessageRepository.findAll(
            DbMessageSpecification.messageHasFromIdOrToId(ownerId, otherId)));
    }
}
