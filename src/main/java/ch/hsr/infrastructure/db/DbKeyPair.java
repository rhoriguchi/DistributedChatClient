package ch.hsr.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity (name = "DbKeyPair")
@Table (name = "KeyPairs")
@Data
@AllArgsConstructor
public class DbKeyPair {

    @Id
    @Column (name = "username")
    private String username;
    @Column (name = "privateKey", length = 2018)
    private String privateKey;
    @Column (name = "publicKey", length = 2018)
    private String publicKey;

    //needed by jpa
    public DbKeyPair() {

    }
}
