package ch.hsr.domain.groupmessage;

import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.keystore.SignState;
import ch.hsr.domain.peer.Peer;
import lombok.Data;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GroupMessage {

    private final GroupMessageId id;
    private final Group group;
    private final Peer fromPeer;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final SignState signState;
    private final Map<Peer, Boolean> failed;

    public GroupMessage(GroupMessageId id,
                        Group group,
                        Peer fromPeer,
                        MessageText text,
                        MessageTimeStamp timeStamp,
                        SignState signState,
                        Map<Peer, Boolean> failed) {
        this.id = id;
        this.group = group;
        this.fromPeer = fromPeer;
        this.text = text;
        this.timeStamp = timeStamp;
        this.signState = signState;
        this.failed = new HashMap<>(failed);
    }

    public static GroupMessage newGroupMessage(Peer fromPeer,
                                               Group group,
                                               MessageText text) {
        return new GroupMessage(
            GroupMessageId.empty(),
            group,
            fromPeer,
            text,
            MessageTimeStamp.now(),
            SignState.VALID,
            group.getMembers().stream()
                .collect(Collectors.toMap(peer -> peer, peer -> false))
        );
    }

    public Collection<Peer> getToPeers() {
        return failed.entrySet().stream()
            .map(Map.Entry::getKey)
            .collect(Collectors.toSet());
    }

    public Map<Peer, Boolean> getFailed() {
        return new HashMap<>(failed);
    }
}
