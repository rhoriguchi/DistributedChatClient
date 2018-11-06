package ch.hsr.domain.group;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.Username;
import lombok.Data;
import java.util.Collection;
import java.util.HashSet;

@Data
public class Group {

    private final GroupId id;
    private final GroupName name;
    private final Collection<Username> memberUsernames;

    public Group(GroupId groupId,
                 GroupName name,
                 Collection<Username> memberUsernames) {
        this.id = groupId;
        this.name = name;
        this.memberUsernames = new HashSet<>(memberUsernames);
    }

    public static Group newGroup(GroupName name,
                                 Collection<Username> members) {
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

    public Collection<Username> getMemberUsernames() {
        return new HashSet<>(memberUsernames);
    }
}
