package ch.hsr.infrastructure.db;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity (name = "DbFriend")
@Table (name = "Friend")
@Data
public class DbFriend {

    @Id
    @Column (name = "id")
    private String id;
    @Column (name = "username")
    private String username;

    //needed by jpa
    public DbFriend() {
    }
}