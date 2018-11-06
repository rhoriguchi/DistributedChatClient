package ch.hsr.domain.common;

import com.google.common.base.Strings;
import java.time.Instant;

public class MessageTimeStamp extends DateValue {

    private MessageTimeStamp(Instant instant) {
        super(instant);
    }

    public static MessageTimeStamp now() {
        return new MessageTimeStamp(Instant.now());
    }

    public static MessageTimeStamp fromString(String messageTimeStamp) {
        if (Strings.isNullOrEmpty(messageTimeStamp)) {
            return empty();
        }
        Instant dateTime = Instant.parse(messageTimeStamp);
        return new MessageTimeStamp(dateTime);
    }

    public static MessageTimeStamp empty() {
        return new MessageTimeStamp(Instant.MIN);
    }

}
