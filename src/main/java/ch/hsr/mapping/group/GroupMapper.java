package ch.hsr.mapping.group;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.group.GroupName;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbGroup;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GroupMapper implements GroupRepository {

    private final DbGateway dbGateway;

    public GroupMapper(DbGateway dbGateway) {
        this.dbGateway = dbGateway;
    }

    @Override
    public void create(Group group) {
        dbGateway.createGroup(
            group.getName().toString(),
            group.getMemberUsernames().stream()
                .map(Username::toString)
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
                .map(Username::fromString)
                .collect(Collectors.toSet())
        );
    }
}
