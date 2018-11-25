package ch.hsr.mapping.group;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import java.util.stream.Stream;

public interface GroupRepository {

    void create(Group group);

    Group get(GroupId groupId);

    //TODO add filter to not load all (paging)
    Stream<Group> getAll(Username username);
}
