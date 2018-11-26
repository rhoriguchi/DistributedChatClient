package ch.hsr.dsa.infrastructure.db;

import org.springframework.data.repository.CrudRepository;

public interface DbKeyStoreRepository extends CrudRepository<DbKeyPair, String> {
}
