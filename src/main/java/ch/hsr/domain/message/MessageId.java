package ch.hsr.domain.message;

import ch.hsr.domain.common.LongValue;

public class MessageId extends LongValue {

    private MessageId(Long messageId) {
        super(messageId);
    }

    public static MessageId fromLong(Long messageId) {
        return new MessageId(messageId);
    }

    public static MessageId empty() {
        return new MessageId(null);
    }
}
