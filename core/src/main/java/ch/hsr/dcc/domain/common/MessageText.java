package ch.hsr.dcc.domain.common;

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

