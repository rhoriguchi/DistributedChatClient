package ch.hsr.domain.message;

import ch.hsr.domain.peer.Peer;
import lombok.Data;

@Data
public class Message {

    private final Peer from;
    private final Peer to;
    private final MessageText messageText;
    private final MessageTimeStamp messageTimeStamp;

    public Message(Peer from, Peer to, MessageText messageText) {
        this.from = from;
        this.to = to;
        this.messageText = messageText;
        messageTimeStamp = MessageTimeStamp.now();
    }
}
