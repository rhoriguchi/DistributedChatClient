package ch.hsr.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity (name = "DbFriend")
@Table (name = "Friends")
@Data
@AllArgsConstructor
public class DbFriend {

    @Id
    @Column (name = "username")
    private String username;
    @Column (name = "ownerUsername")
    private String ownerUsername;

    //needed by jpa
    public DbFriend() {
    }
}