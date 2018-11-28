package ch.hsr.dcc.infrastructure.db;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DbFriendRepository extends CrudRepository<DbFriend, String>, JpaSpecificationExecutor<DbFriend> {

    Iterable<DbFriend> findByOwnerUsername(String username);

    Iterable<DbFriend> findByState(String state);

}
