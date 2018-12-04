package ch.hsr.dcc.domain.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.peer.Peer;
import lombok.Data;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Data
public class Group {

    private final GroupId id;
    private final GroupName name;
    private final Peer admin;
    private final GroupChangedTimeStamp lastChanged;
    private Collection<Peer> members;

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

    public static Group empty(GroupId id) {
        return new Group(
            id,
            GroupName.empty(),
            Peer.empty(),
            new HashSet<>(),
            GroupChangedTimeStamp.now()
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
}
