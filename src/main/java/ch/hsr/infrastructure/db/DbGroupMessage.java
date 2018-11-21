package ch.hsr.infrastructure.db;

import lombok.Data;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity (name = "DbGroupMessage")
@Table (name = "GroupMessages")
@Data
public class DbGroupMessage {

    @Id
    @Column (name = "id")
    private Long id;
    @Column (name = "fromUsername")
    private String fromUsername;
    @Column (name = "toGroupId")
    private Long toGroupId;
    @Column (name = "text")
    private String text;
    @Column (name = "timeStamp")
    private String timeStamp;
    @Column (name = "states")
    @ElementCollection (fetch = FetchType.EAGER)
    private Map<String, String> states;
    @Column (name = "signState")
    private String signState;

    //needed by jpa
    public DbGroupMessage() {

    }

    public DbGroupMessage(Long id,
                          String fromUsername,
                          Long toGroupId,
                          String text,
                          String timeStamp,
                          Map<String, String> states,
                          String signState) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toGroupId = toGroupId;
        this.text = text;
        this.timeStamp = timeStamp;
        this.states = new HashMap<>(states);
        this.signState = signState;
    }

    public Map<String, String> getStates() {
        return new HashMap<>(states);
    }
}
