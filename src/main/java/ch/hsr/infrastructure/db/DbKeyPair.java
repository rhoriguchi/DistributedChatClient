package ch.hsr.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity (name = "DbKeyPair")
@Table (name = "KeyPairs")
@Data
@AllArgsConstructor
public class DbKeyPair {

    @Id
    @Column (name = "username")
    private String username;
    @Lob
    @Column (name = "privateKey")
    private String privateKey;
    @Lob
    @Column (name = "publicKey")
    private String publicKey;

    //needed by jpa
    public DbKeyPair() {

    }
}
