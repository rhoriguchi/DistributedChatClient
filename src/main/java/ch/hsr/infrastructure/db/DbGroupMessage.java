package ch.hsr.infrastructure.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@Entity (name = "DbGroupMessage")
@Table (name = "GroupMessages")
@Data
@AllArgsConstructor
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

    public static DbGroupMessage newDbGroupMessage(Long id,
                                                   String fromUsername,
                                                   Long toGroupId,
                                                   String text,
                                                   String timeStamp,
                                                   Map<String, String> states,
                                                   String signState) {
        return new DbGroupMessage(
            id,
            fromUsername,
            toGroupId,
            text,
            timeStamp,
            new HashMap<>(states),
            signState
        );
    }

    public Map<String, String> getStates() {
        return new HashMap<>(states);
    }
}
