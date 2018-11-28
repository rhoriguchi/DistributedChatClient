package ch.hsr.dcc.mapping.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import java.util.stream.Stream;

public interface GroupRepository {

    void create(Group group);

    Group get(GroupId groupId);

    Stream<Group> getAll(Username username);
}
