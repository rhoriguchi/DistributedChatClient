package ch.hsr.dsa.domain.message;

import ch.hsr.dsa.domain.common.MessageText;
import ch.hsr.dsa.domain.common.MessageTimeStamp;
import ch.hsr.dsa.domain.keystore.SignState;
import ch.hsr.dsa.domain.peer.Peer;
import lombok.Data;

@Data
public class Message {

    private final MessageId id;
    private final Peer fromPeer;
    private final Peer toPeer;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final SignState signState;
    private final boolean failed;

    public static Message newMessage(Peer fromPeer,
                                     Peer toPeer,
                                     MessageText text) {
        return new Message(
            MessageId.empty(),
            fromPeer,
            toPeer,
            text,
            MessageTimeStamp.now(),
            SignState.VALID,
            false
        );
    }
}
