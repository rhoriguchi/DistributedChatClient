package ch.hsr.dsa.infrastructure.db;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class DbGroup {

    @Id
    @GeneratedValue
    @Column (name = "id")
    private Long id;
    @Column (name = "name")
    private String name;
    @Column (name = "members")
    @ElementCollection (fetch = FetchType.EAGER)
    private Collection<String> members;

    //needed by jpa
    public DbGroup() {
    }

    public DbGroup(String name, Collection<String> members) {
        this.name = name;
        this.members = new HashSet<>(members);
    }

    public Collection<String> getMembers() {
        return new HashSet<>(members);
    }
}
