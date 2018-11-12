package ch.hsr.domain.message;

import ch.hsr.domain.common.MessageState;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Username;
import lombok.Data;

@Data
public class Message {

    private final MessageId id;
    private final Username fromUsername;
    private final Username toUsername;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final MessageState state;

    public static Message newMessage(Username fromUsername,
                                     Username toUsername,
                                     MessageText text) {
        return new Message(
            MessageId.empty(),
            fromUsername,
            toUsername,
            text,
            MessageTimeStamp.now(),
            MessageState.SENT
        );
    }
}
