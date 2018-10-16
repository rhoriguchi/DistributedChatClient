package ch.hsr.domain.message;

import ch.hsr.domain.common.StringValue;

public class MessageText extends StringValue {

    private MessageText(String messageText) {
        super(messageText);
    }

    public static MessageText fromString(String messageText) {
        return new MessageText(messageText);
    }

    public static MessageText empty() {
        return new MessageText("");
    }
}

