package ch.hsr.dsa.mapping.group;

import ch.hsr.dsa.domain.common.GroupId;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.group.Group;
import java.util.stream.Stream;

public interface GroupRepository {

    void create(Group group);

    Group get(GroupId groupId);

    Stream<Group> getAll(Username username);
}
