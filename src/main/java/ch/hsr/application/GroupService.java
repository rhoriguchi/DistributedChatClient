package ch.hsr.application;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.group.GroupName;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.peer.PeerRepository;
import java.util.Set;
import java.util.stream.Stream;

// TODO no view exists that uses this
public class GroupService {

    private final GroupRepository groupRepository;
    private final PeerRepository peerRepository;

    public GroupService(GroupRepository groupRepository, PeerRepository peerRepository) {
        this.groupRepository = groupRepository;
        this.peerRepository = peerRepository;
    }

    // TODO notify all peers that they are now in a group
    public void addGroup(GroupName groupName, Set<Username> members) {
        members.add(peerRepository.getSelf().getUsername());

        groupRepository.create(Group.newGroup(groupName, members));
    }

    public Stream<Group> getAllGroups() {
        Username username = peerRepository.getSelf().getUsername();

        return groupRepository.getAll(username);
    }
}
