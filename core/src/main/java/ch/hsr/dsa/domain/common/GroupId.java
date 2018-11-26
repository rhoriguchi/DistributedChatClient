package ch.hsr.dsa.domain.common;

public class GroupId extends LongValue {

    private GroupId(Long groupId) {
        super(groupId);
    }

    public static GroupId fromLong(Long groupId) {
        return new GroupId(groupId);
    }

    public static GroupId empty() {
        return new GroupId(null);
    }
}
