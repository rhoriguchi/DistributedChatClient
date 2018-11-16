package ch.hsr.domain.message;

import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Peer;
import lombok.Data;

@Data
public class Message {

    private final MessageId id;
    private final Peer fromPeer;
    private final Peer toPeer;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final boolean received;

    public static Message newMessage(Peer fromPeer,
                                     Peer toPeer,
                                     MessageText text) {
        return new Message(
            MessageId.empty(),
            fromPeer,
            toPeer,
            text,
            MessageTimeStamp.now(),
            false
        );
    }
}
