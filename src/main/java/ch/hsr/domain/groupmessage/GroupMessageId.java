package ch.hsr.domain.groupmessage;

import ch.hsr.domain.common.LongValue;

public class GroupMessageId extends LongValue {

    private GroupMessageId(Long groupMessageId) {
        super(groupMessageId);
    }

    public static GroupMessageId fromLong(Long groupMessageId) {
        return new GroupMessageId(groupMessageId);
    }

    public static GroupMessageId empty() {
        return new GroupMessageId(null);
    }
}
