package ch.hsr.dcc.domain.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.peer.Peer;
import lombok.Data;
import java.util.Collection;
import java.util.HashSet;

@Data
public class Group {

    private final GroupId id;
    private final GroupName name;
    private final Peer admin;
    private final Collection<Peer> members;
    private final GroupChangedTimeStamp lastChanged;

    public Group(GroupId groupId,
                 GroupName name,
                 Peer admin,
                 Collection<Peer> members,
                 GroupChangedTimeStamp lastChanged) {
        this.id = groupId;
        this.name = name;
        this.admin = admin;
        this.members = new HashSet<>(members);
        this.lastChanged = lastChanged;
    }

    public static Group newGroup(GroupName name,
                                 Peer admin) {
        return new Group(
            GroupId.empty(),
            name,
            admin,
            new HashSet<>(),
            GroupChangedTimeStamp.now()
        );
    }

    public Collection<Peer> getMembers() {
        return new HashSet<>(members);
    }
}
