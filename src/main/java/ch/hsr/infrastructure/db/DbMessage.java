package ch.hsr.infrastructure.db;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity (name = "DbMessage")
@Table (name = "Messages")
@Data
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
    @Column (name = "timeStamp")
    private String timeStamp;

    //needed by jpa
    public DbMessage() {
    }

    public DbMessage(String fromUsername, String toUsername, String text, String timeStamp) {
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.text = text;
        this.timeStamp = timeStamp;
    }
}
