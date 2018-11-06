package ch.hsr.mapping.message;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.groupmessage.GroupMessage;
import ch.hsr.domain.message.Message;
import java.util.stream.Stream;

public interface MessageRepository {

    void send(Message message);

    // TODO add filter to not load all (paging)
    Stream<Message> getAllMessages(Username ownerUsername, Username otherUsername);

    void receivedMessage();

    void send(GroupMessage groupMessage);

    // TODO add filter to not load all (paging)
    Stream<GroupMessage> getAllGroupMessages(Username username);

    void receivedGroupMessage();
}
