package ch.hsr.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

public interface DbGroupMessageRepository extends CrudRepository<DbGroupMessage, Long> {

    Iterable<DbGroupMessage> findByToGroupId(Long toGroupId);

}
