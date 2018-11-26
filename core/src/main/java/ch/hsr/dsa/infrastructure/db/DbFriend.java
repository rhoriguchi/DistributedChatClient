package ch.hsr.dsa.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity (name = "DbFriend")
@Table (name = "Friends")
@IdClass (CompositeId.class)
@Data
@AllArgsConstructor
public class DbFriend {

    @Id
    @Column (name = "username")
    private String username;
    @Id
    @Column (name = "ownerUsername")
    private String ownerUsername;
    @Column (name = "state")
    private String state;
    @Column (name = "failed")
    private boolean failed;

    //needed by jpa
    public DbFriend() {
    }

    public static DbFriend newDbFriend(String username,
                                       String ownerUsername,
                                       String state,
                                       boolean failed) {
        return new DbFriend(
            username,
            ownerUsername,
            state,
            false
        );
    }
}

class CompositeId implements Serializable {

    private static final long serialVersionUID = -735629893458300852L;

    private String username;
    private String ownerUsername;

}