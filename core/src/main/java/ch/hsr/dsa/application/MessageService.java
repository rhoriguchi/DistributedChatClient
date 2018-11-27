package ch.hsr.dsa.application;

import ch.hsr.dsa.application.exception.MessageException;
import ch.hsr.dsa.domain.common.GroupId;
import ch.hsr.dsa.domain.common.MessageText;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.group.Group;
import ch.hsr.dsa.domain.groupmessage.GroupMessage;
import ch.hsr.dsa.domain.message.Message;
import ch.hsr.dsa.domain.peer.Peer;
import ch.hsr.dsa.mapping.group.GroupRepository;
import ch.hsr.dsa.mapping.message.MessageRepository;
import ch.hsr.dsa.mapping.peer.PeerRepository;
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

    public void sendMessage(Username toUsername, MessageText messageText) {
        if (!messageText.isEmpty()) {
            if (!toUsername.isEmpty()) {
                Peer fromPeer = peerRepository.getSelf();

                if (!fromPeer.getUsername().equals(toUsername)) {
                    Peer toPeer = peerRepository.get(toUsername);

                    messageRepository.send(Message.newMessage(
                        fromPeer,
                        toPeer,
                        messageText
                    ));
                } else {
                    throw new MessageException("Messages can't be sent to yourself");
                }
            } else {
                throw new MessageException("Can't send message to empty username");
            }
        } else {
            throw new MessageException("Can't send empty message");
        }
    }

    public Stream<Message> getAllMessages(Username username) {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        return messageRepository.getAllMessages(ownerUsername, username);
    }

    public void messageReceived() {
        Message message = messageRepository.oldestReceivedMessage();
        messageRepository.createMessage(message);
    }

    public void groupMessageReceived() {
        GroupMessage groupMessage = messageRepository.oldestReceivedGroupMessage();
        messageRepository.createGroupMessage(groupMessage);
    }

    public void sendGroupMessage(GroupId toGroupId, MessageText messageText) {
        if (!messageText.isEmpty()) {
            if (!toGroupId.isEmpty()) {
                Peer fromPeer = peerRepository.getSelf();
                Group toGroup = groupRepository.get(toGroupId);

                messageRepository.send(GroupMessage.newGroupMessage(
                    fromPeer,
                    toGroup,
                    messageText
                ));
            } else {
                throw new MessageException("Can't send group message to empty group id");
            }
        } else {
            throw new MessageException("Can't send empty message");
        }
    }

    public Stream<GroupMessage> getAllGroupMessages(Username username) {
        return messageRepository.getAllGroupMessages(username);
    }
}
