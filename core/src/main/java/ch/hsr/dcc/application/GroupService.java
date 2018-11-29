package ch.hsr.dcc.application;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.group.GroupName;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.scheduling.annotation.Async;
import java.util.stream.Stream;

//TODO use scheduler to check if update of group members
//TOOD use scheduler to add newest version to dht
public class GroupService {

    private final GroupRepository groupRepository;
    private final PeerRepository peerRepository;

    public GroupService(GroupRepository groupRepository, PeerRepository peerRepository) {
        this.groupRepository = groupRepository;
        this.peerRepository = peerRepository;
    }

    @Async
    public void createGroup(GroupName groupName) {
        Peer self = peerRepository.getSelf();

        groupRepository.save(Group.newGroup(groupName, self));
    }

    @Async
    public void removeGroupMember(GroupId groupId, Username username) {
        Peer self = peerRepository.getSelf();
        Group group = groupRepository.get(groupId)
            //TODO wrong exception
            .orElseThrow(() -> new IllegalArgumentException("Group does not exist"));

        if (group.getAdmin().getUsername().equals(self.getUsername())) {
            Peer peer = peerRepository.get(username);
            group.removeMember(peer);

            groupRepository.save(group);
        } else {
            //TODO wrong exception
            throw new IllegalArgumentException(String.format("You are not admin of this group, admin is %s",
                group.getAdmin().getUsername()));
        }
    }

    @Async
    public void addGroupMember(GroupId groupId, Username username) {
        Peer self = peerRepository.getSelf();
        Group group = groupRepository.get(groupId)
            //TODO wrong exception
            .orElseThrow(() -> new IllegalArgumentException("Group does not exist"));

        if (group.getAdmin().getUsername().equals(self.getUsername())) {
            Peer peer = peerRepository.get(username);
            group.addMember(peer);

            groupRepository.save(group);
        } else {
            //TODO wrong exception
            throw new IllegalArgumentException(String.format("You are not admin of this group, admin is %s",
                group.getAdmin().getUsername()));
        }
    }

    public Stream<Group> getAllGroups() {
        Username username = peerRepository.getSelf().getUsername();

        return groupRepository.getAll(username);
    }
}
