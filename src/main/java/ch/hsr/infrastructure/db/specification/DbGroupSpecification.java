package ch.hsr.infrastructure.db.specification;

import ch.hsr.infrastructure.db.DbGroup;
import ch.hsr.infrastructure.db.DbGroup_;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.springframework.data.jpa.domain.Specification.where;

public class DbGroupSpecification {

    public static Specification<DbGroup> groupContainsMember(String username) {
        return where(groupHasMembersUsername(username));
    }

    // TODO not tested
    private static Specification<DbGroup> groupHasMembersUsername(String username) {
        return new Specification<DbGroup>() {
            @Override
            public Predicate toPredicate(Root<DbGroup> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.isMember(username, root.get(DbGroup_.members));
            }
        };
    }
}
