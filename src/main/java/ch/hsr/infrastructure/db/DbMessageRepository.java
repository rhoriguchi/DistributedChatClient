package ch.hsr.infrastructure.db;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DbMessageRepository extends CrudRepository<DbMessage, String>, JpaSpecificationExecutor<DbMessage> {

}