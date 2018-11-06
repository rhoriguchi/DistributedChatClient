package ch.hsr.domain.message;

import ch.hsr.domain.common.PeerId;
import lombok.Data;

@Data
public class Message {

    private final MessageId id;
    private final PeerId fromId;
    private final PeerId toId;
    private final MessageText text;
    private final MessageTimeStamp messageTimeStamp;

    public static Message newMessage(PeerId fromId, PeerId toId, MessageText text) {
        return new Message(
            MessageId.empty(),
            fromId,
            toId,
            text,
            MessageTimeStamp.now()
        );
    }
}
