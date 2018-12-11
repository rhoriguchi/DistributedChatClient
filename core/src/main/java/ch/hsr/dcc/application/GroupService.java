package ch.hsr.dcc.application;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.group.GroupName;
import ch.hsr.dcc.domain.keystore.SignState;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.mapping.exception.GroupException;
import ch.hsr.dcc.mapping.exception.SignException;
import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.scheduling.annotation.Async;
import java.util.stream.Stream;

public class GroupService {

    private final GroupRepository groupRepository;
    private final PeerRepository peerRepository;
    private final KeyStoreRepository keyStoreRepository;

    public GroupService(GroupRepository groupRepository, PeerRepository peerRepository, KeyStoreRepository keyStoreRepository) {
        this.groupRepository = groupRepository;
        this.peerRepository = peerRepository;
        this.keyStoreRepository = keyStoreRepository;
    }

    @Async
    public void createGroup(GroupName groupName) {
        Peer self = peerRepository.getSelf();

        Group group = Group.newGroup(groupName, self);
        group.setSign(keyStoreRepository.sign(group));

        groupRepository.save(group);
    }

    @Async
    public void removeGroupMember(GroupId groupId, Username username) {
        Peer self = peerRepository.getSelf();
        Group group = groupRepository.get(groupId)
            .orElseThrow(() -> new GroupException("Group does not exist"));

        if (group.getAdmin().getUsername().equals(self.getUsername())) {
            Peer peer = peerRepository.get(username);
            group.removeMember(peer);
            group.setSign(keyStoreRepository.sign(group));

            groupRepository.save(group);
        } else {
            throw new GroupException(String.format("You are not adminUsername of this group, adminUsername is %s",
                group.getAdmin().getUsername()));
        }
    }

    @Async
    public void addGroupMember(GroupId groupId, Username username) {
        Peer self = peerRepository.getSelf();
        Group group = groupRepository.get(groupId)
            .orElseThrow(() -> new GroupException("Group does not exist"));

        if (group.getAdmin().getUsername().equals(self.getUsername())) {
            Peer peer = peerRepository.get(username);

            if (peer.isOnline()) {
                group.addMember(peer);
                group.setSign(keyStoreRepository.sign(group));

                groupRepository.sendGroupAdd(group, peer);
                groupRepository.save(group);
            } else {
                throw new GroupException("User you want to add is not online");
            }
        } else {
            throw new GroupException(String.format("You are not adminUsername of this group, adminUsername is %s",
                group.getAdmin().getUsername()));
        }
    }

    public Stream<Group> getAllGroups() {
        Username username = peerRepository.getSelf().getUsername();

        return groupRepository.getAll(username);
    }

    @Async
    public void groupAddReceived() {
        Group group = groupRepository.getOldestGroupAdd();

        if (keyStoreRepository.checkSignature(peerRepository.getSelf().getUsername(), group) == SignState.VALID) {
            groupRepository.addGroup(group);
        } else {
            throw new SignException(String.format("GroupAdd signature is invalid %s", group));
        }
    }
}
