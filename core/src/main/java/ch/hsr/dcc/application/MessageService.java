package ch.hsr.dcc.application;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.MessageText;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.groupmessage.GroupMessage;
import ch.hsr.dcc.domain.keystore.SignState;
import ch.hsr.dcc.domain.message.Message;
import ch.hsr.dcc.domain.message.MessageId;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.mapping.exception.MessageException;
import ch.hsr.dcc.mapping.exception.SignException;
import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.message.MessageRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.springframework.scheduling.annotation.Async;
import java.util.stream.Stream;

public class MessageService {

    private final MessageRepository messageRepository;
    private final GroupRepository groupRepository;
    private final PeerRepository peerRepository;
    private final KeyStoreRepository keyStoreRepository;

    public MessageService(MessageRepository messageRepository,
                          GroupRepository groupRepository,
                          PeerRepository peerRepository,
                          KeyStoreRepository keyStoreRepository) {
        this.messageRepository = messageRepository;
        this.groupRepository = groupRepository;
        this.peerRepository = peerRepository;
        this.keyStoreRepository = keyStoreRepository;
    }

    @Async
    public void sendMessage(Username toUsername, MessageText messageText) {
        if (!messageText.isEmpty()) {
            if (!toUsername.isEmpty()) {
                Peer fromPeer = peerRepository.getSelf();

                if (!fromPeer.getUsername().equals(toUsername)) {
                    Peer toPeer = peerRepository.get(toUsername);

                    messageRepository.send(
                        Message.newMessage(
                            fromPeer,
                            toPeer,
                            messageText
                        )
                    );
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

    public Message getMessage(MessageId messageId) {
        return messageRepository.getMessage(messageId)
            .orElseThrow(() -> new MessageException(String.format("Message %s does not exist", messageId)));
    }

    @Async
    public void messageReceived() {
        Message message = messageRepository.oldestReceivedMessage();

        if (keyStoreRepository.checkSignature(peerRepository.getSelf().getUsername(), message) == SignState.VALID) {
            messageRepository.saveMessage(message);
        } else {
            throw new SignException(String.format("Message signature is invalid %s", message));
        }
    }

    @Async
    public void groupMessageReceived() {
        GroupMessage groupMessage = messageRepository.oldestReceivedGroupMessage();

        if (keyStoreRepository.checkSignature(peerRepository.getSelf().getUsername(),
            groupMessage) == SignState.VALID) {
            messageRepository.saveGroupMessage(groupMessage);
        } else {
            throw new SignException(String.format("Message signature is invalid %s", groupMessage));
        }
    }

    @Async
    public void sendGroupMessage(GroupId toGroupId, MessageText messageText) {
        if (!messageText.isEmpty()) {
            if (!toGroupId.isEmpty()) {
                Peer fromPeer = peerRepository.getSelf();

                Group toGroup = groupRepository.get(toGroupId)
                    .orElseGet(() -> Group.empty(toGroupId));

                messageRepository.send(
                    GroupMessage.newGroupMessage(
                        fromPeer,
                        toGroup,
                        messageText
                    )
                );
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