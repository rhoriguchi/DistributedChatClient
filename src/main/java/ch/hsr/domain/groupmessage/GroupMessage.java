package ch.hsr.domain.groupmessage;

import ch.hsr.domain.common.MessageState;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Peer;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.keystore.SignState;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GroupMessage {

    private final GroupMessageId id;
    private final Peer fromPeer;
    private final Group toGroup;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final Map<Peer, MessageState> states;
    private final SignState signState;

    public GroupMessage(GroupMessageId id,
                        Peer fromPeer,
                        Group toGroup,
                        MessageText text,
                        MessageTimeStamp timeStamp,
                        Map<Peer, MessageState> states,
                        SignState signState) {
        this.id = id;
        this.fromPeer = fromPeer;
        this.toGroup = toGroup;
        this.text = text;
        this.timeStamp = timeStamp;
        this.states = new HashMap<>(states);
        this.signState = signState;
    }

    public static GroupMessage newGroupMessage(Peer fromPeer,
                                               Group toGroup,
                                               MessageText text) {
        return new GroupMessage(
            GroupMessageId.empty(),
            fromPeer,
            toGroup,
            text,
            MessageTimeStamp.now(),
            toGroup.getMembers().stream()
                .collect(Collectors.toMap(peer -> peer, peer -> MessageState.SENT)),
            SignState.VALID
        );
    }

    public Map<Peer, MessageState> getStates() {
        return new HashMap<>(states);
    }
}
