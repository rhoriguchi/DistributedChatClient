package ch.hsr.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

public interface DbFriendRepository extends CrudRepository<DbFriend, String> {

    Iterable<DbFriend> findByOwnerId(String ownerId);

}
