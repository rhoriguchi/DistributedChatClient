package ch.hsr.dcc.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

public interface DbGroupMessageRepository extends CrudRepository<DbGroupMessage, Long> {

    Iterable<DbGroupMessage> findByGroupId(Long toGroupId);

}
