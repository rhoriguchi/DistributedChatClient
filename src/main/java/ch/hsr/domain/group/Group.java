package ch.hsr.domain.group;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.Peer;
import lombok.Data;
import java.util.Collection;
import java.util.HashSet;

@Data
public class Group {

    private final GroupId id;
    private final GroupName name;
    private final Collection<Peer> members;
    // TODO add logic to have state (enum), acknowledged, sent,...

    public Group(GroupId groupId,
                 GroupName name,
                 Collection<Peer> members) {
        this.id = groupId;
        this.name = name;
        this.members = new HashSet<>(members);
    }

    public static Group newGroup(GroupName name,
                                 Collection<Peer> members) {
        return new Group(
            GroupId.empty(),
            name,
            new HashSet<>(members)
        );
    }

    public static Group empty() {
        return new Group(
            GroupId.empty(),
            GroupName.empty(),
            new HashSet<>()
        );
    }

    public Collection<Peer> getMembers() {
        return new HashSet<>(members);
    }
}
