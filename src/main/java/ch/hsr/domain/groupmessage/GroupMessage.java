package ch.hsr.domain.groupmessage;

import ch.hsr.domain.common.MessageState;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Peer;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
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
    private final Map<Username, MessageState> states;
    private final boolean valid;

    public GroupMessage(GroupMessageId id,
                        Peer fromPeer,
                        Group toGroup,
                        MessageText text,
                        MessageTimeStamp timeStamp,
                        Map<Username, MessageState> states,
                        boolean valid) {
        this.id = id;
        this.fromPeer = fromPeer;
        this.toGroup = toGroup;
        this.text = text;
        this.timeStamp = timeStamp;
        this.states = new HashMap<>(states);
        this.valid = valid;
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
                .map(Peer::getUsername)
                .collect(Collectors.toMap(username -> username, username -> MessageState.SENT)),
            true
        );
    }

    public Map<Username, MessageState> getStates() {
        return new HashMap<>(states);
    }
}
