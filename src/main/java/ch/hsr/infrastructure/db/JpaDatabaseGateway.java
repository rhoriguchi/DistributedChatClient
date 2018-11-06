package ch.hsr.infrastructure.db;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class JpaDatabaseGateway implements DbGateway {

    private final DbFriendRepository dbFriendRepository;

    public JpaDatabaseGateway(DbFriendRepository dbFriendRepository) {
        this.dbFriendRepository = dbFriendRepository;
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
}
