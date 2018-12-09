package ch.hsr.dcc.domain.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.keystore.Sign;
import ch.hsr.dcc.domain.peer.Peer;
import lombok.Data;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
public class Group {

    private final GroupId id;
    private final GroupName name;
    private final Peer admin;
    private final GroupChangedTimeStamp lastChanged;
    private Collection<Peer> members;
    private Sign sign;

    public Group(GroupId groupId,
                 GroupName name,
                 Peer admin,
                 Collection<Peer> members,
                 GroupChangedTimeStamp lastChanged,
                 Sign sign) {
        this.id = groupId;
        this.name = name;
        this.admin = admin;
        this.members = new HashSet<>(members);
        this.lastChanged = lastChanged;
        this.sign = sign;
    }

    public static Group newGroup(GroupName name,
                                 Peer admin) {
        return new Group(
            GroupId.empty(),
            name,
            admin,
            new HashSet<>(),
            GroupChangedTimeStamp.now(),
            Sign.empty()
        );
    }

    public static Group empty(GroupId id) {
        return new Group(
            id,
            GroupName.empty(),
            Peer.empty(),
            new HashSet<>(),
            GroupChangedTimeStamp.now(),
            Sign.empty()
        );
    }

    public Collection<Peer> getMembers() {
        return new HashSet<>(members);
    }

    public void addMember(Peer peer) {
        boolean contained = members.stream()
            .anyMatch(member -> member.getUsername().equals(peer.getUsername()));

        if (!contained) {
            members.add(peer);
        }
    }

    public void removeMember(Peer peer) {
        members = members.stream()
            .filter(member -> !member.getUsername().equals(peer.getUsername()))
            .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Group group = (Group) o;
        return Objects.equals(id, group.id) &&
            Objects.equals(name, group.name) &&
            Objects.equals(admin, group.admin) &&
            Objects.equals(lastChanged, group.lastChanged) &&
            Objects.equals(members, group.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, admin, lastChanged, members);
    }
}
