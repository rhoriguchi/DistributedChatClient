package ch.hsr.domain.groupmessage;

import ch.hsr.domain.common.MessageState;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GroupMessage {

    private final GroupMessageId id;
    private final Username fromUsername;
    private final Group toGroup;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final Map<Username, MessageState> states;

    public GroupMessage(GroupMessageId id,
                        Username fromUsername,
                        Group toGroup,
                        MessageText text,
                        MessageTimeStamp timeStamp,
                        Map<Username, MessageState> states) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toGroup = toGroup;
        this.text = text;
        this.timeStamp = timeStamp;
        this.states = new HashMap<>(states);
    }

    public static GroupMessage newGroupMessage(Username fromUsername,
                                               Group toGroup,
                                               MessageText text) {
        return new GroupMessage(
            GroupMessageId.empty(),
            fromUsername,
            toGroup,
            text,
            MessageTimeStamp.now(),
            toGroup.getMemberUsernames().stream()
                .collect(Collectors.toMap(username -> username, username -> MessageState.SENT))
        );
    }

    public Map<Username, MessageState> getStates() {
        return new HashMap<>(states);
    }
}
