package ch.hsr.dcc.domain.group;

import ch.hsr.dcc.domain.common.StringValue;

public class GroupName extends StringValue {

    private GroupName(String groupName) {
        super(groupName);
    }

    public static GroupName fromString(String groupName) {
        return new GroupName(groupName);
    }

    public static GroupName empty() {
        return new GroupName("");
    }
}

