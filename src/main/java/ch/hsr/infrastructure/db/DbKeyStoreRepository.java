package ch.hsr.infrastructure.db;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DbKeyStoreRepository extends CrudRepository<DbKeyPair, String>, JpaSpecificationExecutor<DbKeyPair> {
}
