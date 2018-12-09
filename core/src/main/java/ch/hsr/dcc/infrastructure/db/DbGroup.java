package ch.hsr.dcc.infrastructure.db;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;

@Entity (name = "DbGroup")
@Table (name = "Groups")
@Data
public class DbGroup {

    @Column (name = "signature")
    public String signature;
    @Id
    @GeneratedValue
    @Column (name = "id")
    private Long id;
    @Column (name = "name")
    private String name;
    @Column (name = "admin")
    private String admin;
    @Column (name = "members")
    @ElementCollection (fetch = FetchType.EAGER)
    private Collection<String> members;
    @Column (name = "lastChanged")
    private String lastChanged;

    //needed by jpa
    public DbGroup() {
    }

    public DbGroup(Long id,
                   String name,
                   String admin,
                   Collection<String> members,
                   String lastChanged,
                   String signature) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.members = new HashSet<>(members);
        this.lastChanged = lastChanged;
        this.signature = signature;
    }

    public Collection<String> getMembers() {
        return new HashSet<>(members);
    }
}
