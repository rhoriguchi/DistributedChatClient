package ch.hsr.dcc.mapping.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.group.GroupName;
import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.db.DbGroup;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupMapper implements GroupRepository {

    private final DbGateway dbGateway;

    private final PeerRepository peerRepository;

    public GroupMapper(DbGateway dbGateway, PeerRepository peerRepository) {
        this.dbGateway = dbGateway;
        this.peerRepository = peerRepository;
    }

    @Override
    public void create(Group group) {
        dbGateway.saveGroup(
            new DbGroup(
                group.getName().toString(),
                group.getMembers().stream()
                    .map(member -> member.getUsername().toString())
                    .collect(Collectors.toSet())
            )
        );
    }

    @Override
    //TODO return optional
    public Group get(GroupId groupId) {
        return dbGateway.getGroup(groupId.toLong())
            .map(this::dbGroupToGroup)
            //TODO wrong exception
            .orElseThrow(() -> new IllegalArgumentException(String.format("Group with id %s does not exist", groupId)));
    }

    private Group dbGroupToGroup(DbGroup dbGroup) {
        return new Group(
            GroupId.fromLong(dbGroup.getId()),
            GroupName.fromString(dbGroup.getName()),
            dbGroup.getMembers().stream()
                .map(username -> peerRepository.get(Username.fromString(username)))
                .collect(Collectors.toSet())
        );
    }

    @Override
    public Stream<Group> getAll(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(this::dbGroupToGroup);
    }
}
