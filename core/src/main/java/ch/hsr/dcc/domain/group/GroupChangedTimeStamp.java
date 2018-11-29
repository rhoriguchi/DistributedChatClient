package ch.hsr.dcc.domain.group;

import ch.hsr.dcc.domain.common.DateValue;
import com.google.common.base.Strings;
import java.time.Instant;

public class GroupChangedTimeStamp extends DateValue {

    private GroupChangedTimeStamp(Instant instant) {
        super(instant);
    }

    public static GroupChangedTimeStamp now() {
        return new GroupChangedTimeStamp(Instant.now());
    }

    public static GroupChangedTimeStamp fromString(String groupChangedTimeStamp) {
        if (Strings.isNullOrEmpty(groupChangedTimeStamp)) {
            return empty();
        }
        Instant dateTime = Instant.parse(groupChangedTimeStamp);
        return new GroupChangedTimeStamp(dateTime);
    }

    public static GroupChangedTimeStamp empty() {
        return new GroupChangedTimeStamp(Instant.MIN);
    }

}
