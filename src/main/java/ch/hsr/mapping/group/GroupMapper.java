package ch.hsr.mapping.group;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.group.GroupName;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbGroup;
import ch.hsr.mapping.peer.PeerMapper;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupMapper implements GroupRepository {

    private final DbGateway dbGateway;

    private final PeerMapper peerMapper;

    public GroupMapper(DbGateway dbGateway, PeerMapper peerMapper) {
        this.dbGateway = dbGateway;
        this.peerMapper = peerMapper;
    }

    @Override
    public void create(Group group) {
        dbGateway.createGroup(
            group.getName().toString(),
            group.getMembers().stream()
                .map(member -> member.getUsername().toString())
                .collect(Collectors.toSet())
        );
    }

    @Override
    public Group get(GroupId groupId) {
        return dbGateway.getGroup(groupId.toLong())
            .map(this::dbGroupToGroup)
            .orElse(Group.empty());
    }

    @Override
    public Stream<Group> getAll(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(this::dbGroupToGroup);
    }

    private Group dbGroupToGroup(DbGroup dbGroup) {
        return new Group(
            GroupId.fromLong(dbGroup.getId()),
            GroupName.fromString(dbGroup.getName()),
            dbGroup.getMembers().stream()
                .map(username -> peerMapper.getPeer(Username.fromString(username)))
                .collect(Collectors.toSet())
        );
    }
}
