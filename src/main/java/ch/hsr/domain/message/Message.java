package ch.hsr.domain.message;

import ch.hsr.domain.user.Username;
import lombok.Data;

@Data
public class Message {

    // TODO use user object
    private final Username from;
    // TODO use user object
    private final Username to;
    private final MessageText messageText;
    private final MessageTimeStamp messageTimeStamp;

    public Message(Username from, Username to, MessageText messageText) {
        this.from = from;
        this.to = to;
        this.messageText = messageText;
        messageTimeStamp = MessageTimeStamp.now();
    }
}
