package ch.hsr.dcc.mapping.group;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import java.util.Optional;
import java.util.stream.Stream;

public interface GroupRepository {

    void save(Group group);

    Optional<Group> get(GroupId groupId);

    Stream<Group> getAll(Username username);

    void synchronizeGroups();
}
