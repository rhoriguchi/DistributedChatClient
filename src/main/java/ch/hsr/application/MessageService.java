package ch.hsr.application;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.Peer;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.groupmessage.GroupMessage;
import ch.hsr.domain.message.Message;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.message.MessageRepository;
import ch.hsr.mapping.peer.PeerRepository;
import java.util.stream.Stream;

public class MessageService {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final PeerRepository peerRepository;

    public MessageService(MessageRepository messageRepository, GroupRepository groupRepository, PeerRepository peerRepository) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.peerRepository = peerRepository;
    }

    public void sendMessage(Username toUsername, MessageText message) {
        Peer fromPeer = peerRepository.getSelf();

        if (!fromPeer.getUsername().equals(toUsername)) {
            Peer toPeer = peerRepository.getPeer(toUsername);

            messageRepository.send(Message.newMessage(
                fromPeer,
                toPeer,
                message
            ));
        } else {
            throw new IllegalArgumentException("Messages can't be sent to yourself");
        }
    }

    public Stream<Message> getAllMessages(Username username) {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        return messageRepository.getAllMessages(ownerUsername, username);
    }

    public void messageReceived() {
        messageRepository.receivedMessage();
    }

    // TODO not used
    public void sendGroupMessage(GroupId toGroupId, MessageText messageText) {
        Peer fromPeer = peerRepository.getSelf();
        Group toGroup = groupRepository.get(toGroupId);

        messageRepository.send(GroupMessage.newGroupMessage(
            fromPeer,
            toGroup,
            messageText
        ));
    }

    // TODO not used
    public Stream<GroupMessage> getAllGroupMessages(Username username) {
        return messageRepository.getAllGroupMessages(username);
    }

    public void groupMessageReceived() {
        messageRepository.receivedGroupMessage();
    }
}
