package ch.hsr.dsa.infrastructure.db.specification;

import ch.hsr.dsa.infrastructure.db.DbMessage;
import ch.hsr.dsa.infrastructure.db.DbMessage_;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.springframework.data.jpa.domain.Specification.where;

public class DbMessageSpecification {

    public static Specification<DbMessage> messageHasFromUsernameOrToUsername(String ownerUsername, String otherUsername) {
        return where(messageHasFromUsername(ownerUsername).and(messageHasToUsername(otherUsername)))
            .or(messageHasFromUsername(otherUsername).and(messageHasToUsername(ownerUsername)));
    }

    private static Specification<DbMessage> messageHasFromUsername(String username) {
        return new Specification<DbMessage>() {
            @Override
            public Predicate toPredicate(Root<DbMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(DbMessage_.fromUsername), username);
            }
        };
    }

    private static Specification<DbMessage> messageHasToUsername(String username) {
        return new Specification<DbMessage>() {
            @Override
            public Predicate toPredicate(Root<DbMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(DbMessage_.toUsername), username);
            }
        };
    }
}
