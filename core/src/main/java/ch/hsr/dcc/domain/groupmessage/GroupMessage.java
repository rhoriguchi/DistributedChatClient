package ch.hsr.dcc.domain.groupmessage;

import ch.hsr.dcc.domain.common.MessageText;
import ch.hsr.dcc.domain.common.MessageTimeStamp;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.keystore.Sign;
import ch.hsr.dcc.domain.keystore.SignState;
import ch.hsr.dcc.domain.peer.Peer;
import lombok.Data;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class GroupMessage {

    private final GroupMessageId id;
    private final Group group;
    private final Peer fromPeer;
    private final MessageText text;
    private final MessageTimeStamp timeStamp;
    private final Sign sign;
    private final Map<Peer, Boolean> failed;

    public GroupMessage(GroupMessageId id,
                        Group group,
                        Peer fromPeer,
                        MessageText text,
                        MessageTimeStamp timeStamp,
                        Sign sign,
                        Map<Peer, Boolean> failed) {
        this.id = id;
        this.group = group;
        this.fromPeer = fromPeer;
        this.text = text;
        this.timeStamp = timeStamp;
        this.sign = sign;
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
            Sign.empty(),
            group.getMembers().stream()
                .collect(Collectors.toMap(peer -> peer, peer -> false))
        );
    }

    public Collection<Peer> getToPeers() {
        return new HashSet<>(failed.keySet());
    }

    public Map<Peer, Boolean> getFailed() {
        return new HashMap<>(failed);
    }
}
