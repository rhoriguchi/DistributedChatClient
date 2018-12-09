package ch.hsr.dcc.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity (name = "DbMessage")
@Table (name = "Messages")
@Data
@AllArgsConstructor
public class DbMessage {

    @Id
    @GeneratedValue
    @Column (name = "id")
    private Long id;
    @Column (name = "fromUsername")
    private String fromUsername;
    @Column (name = "toUsername")
    private String toUsername;
    @Column (name = "text")
    private String text;
    @Column (name = "lastChanged")
    private String timeStamp;
    @Column (name = "signature")
    private String signature;
    @Column (name = "failed")
    private boolean failed;

    //needed by jpa
    public DbMessage() {

    }

    public static DbMessage newDbMessage(String fromUsername,
                                         String toUsername,
                                         String text,
                                         String timeStamp,
                                         String signature,
                                         boolean failed) {
        return new DbMessage(
            null,
            fromUsername,
            toUsername,
            text,
            timeStamp,
            signature,
            failed
        );
    }

}
