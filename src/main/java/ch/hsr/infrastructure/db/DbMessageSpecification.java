package ch.hsr.infrastructure.db;

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.springframework.data.jpa.domain.Specification.where;

public class DbMessageSpecification {

    public static Specification<DbMessage> messageHasFromIdOrToId(String ownerId, String otherId) {
        return where(messageHasFromId(ownerId).and(messageHasToId(otherId)))
            .or(messageHasFromId(otherId).and(messageHasToId(ownerId)));
    }

    private static Specification<DbMessage> messageHasFromId(String id) {
        return new Specification<DbMessage>() {
            @Override
            public Predicate toPredicate(Root<DbMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(DbMessage_.fromId), id);
            }
        };
    }

    private static Specification<DbMessage> messageHasToId(String id) {
        return new Specification<DbMessage>() {
            @Override
            public Predicate toPredicate(Root<DbMessage> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(DbMessage_.toId), id);
            }
        };
    }
}
