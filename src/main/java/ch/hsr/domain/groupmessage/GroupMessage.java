package ch.hsr.domain.groupmessage;

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
    private final Map<Username, Boolean> received;

    public GroupMessage(GroupMessageId id,
                        Username fromUsername,
                        Group toGroup,
                        MessageText text,
                        MessageTimeStamp timeStamp,
                        Map<Username, Boolean> received) {
        this.id = id;
        this.fromUsername = fromUsername;
        this.toGroup = toGroup;
        this.text = text;
        this.timeStamp = timeStamp;
        this.received = new HashMap<>(received);
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
                .collect(Collectors.toMap(username -> username, username -> Boolean.FALSE))
        );
    }

    public Map<Username, Boolean> getReceived() {
        return new HashMap<>(received);
    }
}
