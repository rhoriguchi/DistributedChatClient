package ch.hsr.dcc.infrastructure.db;

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
    @GeneratedValue
    @Column (name = "id")
    private Long id;
    @Column (name = "groupId")
    private Long groupId;
    @Column (name = "fromUsername")
    private String fromUsername;
    @Column (name = "text")
    private String text;
    @Column (name = "lastChanged")
    private String timeStamp;
    @Column (name = "failed")
    @ElementCollection (fetch = FetchType.EAGER)
    private Map<String, Boolean> failed;

    //needed by jpa
    public DbGroupMessage() {

    }

    public static DbGroupMessage newDbGroupMessage(Long groupId,
                                                   String fromUsername,
                                                   String text,
                                                   String timeStamp,
                                                   Map<String, Boolean> failed) {
        return new DbGroupMessage(
            null,
            groupId,
            fromUsername,
            text,
            timeStamp,
            new HashMap<>(failed)
        );
    }
}
