package ch.hsr.dcc.infrastructure.db.specification;

import ch.hsr.dcc.infrastructure.db.DbFriend;
import ch.hsr.dcc.infrastructure.db.DbFriend_;
import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import static org.springframework.data.jpa.domain.Specification.where;

public class DbFriendSpecification {

    public static Specification<DbFriend> getFriend(String username, String ownerUsername) {
        return where(friendHasUsername(username))
            .and(friendHasOwnerUsername(ownerUsername));
    }

    private static Specification<DbFriend> friendHasUsername(String username) {
        return new Specification<DbFriend>() {
            @Override
            public Predicate toPredicate(Root<DbFriend> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(DbFriend_.username), username);
            }
        };
    }

    private static Specification<DbFriend> friendHasOwnerUsername(String ownerUsername) {
        return new Specification<DbFriend>() {
            @Override
            public Predicate toPredicate(Root<DbFriend> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get(DbFriend_.ownerUsername), ownerUsername);
            }
        };
    }
}
