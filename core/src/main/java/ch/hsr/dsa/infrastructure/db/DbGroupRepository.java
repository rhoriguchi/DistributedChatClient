package ch.hsr.dsa.infrastructure.db;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface DbGroupRepository extends CrudRepository<DbGroup, Long>, JpaSpecificationExecutor<DbGroup> {

    Iterable<DbGroup> findByMembers(String ownerId);
}
