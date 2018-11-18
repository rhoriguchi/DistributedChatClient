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
        Message message = messageRepository.receivedMessage();

        // TODO handle
        /*dbGateway.getMessage(tomP2PMessage.getId())
            .ifPresent(message -> {
                TomP2PMessageState tomP2PMessageState = TomP2PMessageState.valueOf(message.getState());

                if (message.getState().equals(MessageState.SENT.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    dbGateway.updateMessage(
                        tomP2PMessage.getId(),
                        tomP2PMessage.getFromUsername(),
                        tomP2PMessage.getToUsername(),
                        tomP2PMessage.getText(),
                        tomP2PMessage.getTimeStamp(),
                        MessageState.RECEIVED.name(),
                        keyStoreRepository.CheckSignature(
                            Username.fromString(tomP2PMessage.getFromUsername()),
                            Sign.fromString(tomP2PMessage.getSignature()),
                            tomP2PMessage.hashCode()
                        )
                    );
                } else if (message.getState().equals(MessageState.RECEIVED.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    throw new IllegalArgumentException(String.format("This message %s was already received",
                        tomP2PMessage.toString()));
                } else if (tomP2PMessageState == TomP2PMessageState.ERROR) {
                    dbGateway.updateMessage(
                        tomP2PMessage.getId(),
                        tomP2PMessage.getFromUsername(),
                        tomP2PMessage.getToUsername(),
                        tomP2PMessage.getText(),
                        tomP2PMessage.getTimeStamp(),
                        MessageState.ERROR.name(),
                        keyStoreRepository.CheckSignature(
                            Username.fromString(tomP2PMessage.getFromUsername()),
                            Sign.fromString(tomP2PMessage.getSignature()),
                            tomP2PMessage.hashCode()
                        )
                    );
                }
            });*/
    }

    public void groupMessageReceived() {
        GroupMessage groupMessage = messageRepository.receivedGroupMessage();

        // TODO handle
        /*dbGateway.getGroupMessage(tomP2PGroupMessage.getId())
            .ifPresent(groupMessage -> {
                TomP2PMessageState tomP2PMessageState =
                    TomP2PMessageState.valueOf(groupMessage.getStates().get(tomP2PGroupMessage.getToUsername()));

                String currentState = groupMessage.getStates().entrySet().stream()
                    .filter(entrySet -> !entrySet.getKey().equals(tomP2PGroupMessage.getToUsername()))
                    .map(Map.Entry::getValue)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "This username \"%s\" is not part of this group message %s",
                        tomP2PGroupMessage.getToUsername(),
                        tomP2PGroupMessage.toString())));

                if (currentState.equals(MessageState.SENT.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    dbGateway.updateGroupMessage(
                        tomP2PGroupMessage.getId(),
                        tomP2PGroupMessage.getFromUsername(),
                        tomP2PGroupMessage.getToGroupId(),
                        tomP2PGroupMessage.getText(),
                        tomP2PGroupMessage.getTimeStamp(),
                        getUpdatedDbGroupMessageState(
                            groupMessage,
                            tomP2PGroupMessage.getToUsername(),
                            MessageState.RECEIVED.name()
                        ),
                        keyStoreRepository.CheckSignature(
                            Username.fromString(tomP2PGroupMessage.getFromUsername()),
                            Sign.fromString(tomP2PGroupMessage.getSignature()),
                            tomP2PGroupMessage.hashCode()
                        )
                    );
                } else if (currentState.equals(MessageState.RECEIVED.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    throw new IllegalArgumentException(String.format("This group message %s was already received",
                        tomP2PGroupMessage.toString()));
                } else if (tomP2PMessageState == TomP2PMessageState.ERROR) {
                    dbGateway.updateGroupMessage(
                        tomP2PGroupMessage.getId(),
                        tomP2PGroupMessage.getFromUsername(),
                        tomP2PGroupMessage.getToGroupId(),
                        tomP2PGroupMessage.getText(),
                        tomP2PGroupMessage.getTimeStamp(),
                        getUpdatedDbGroupMessageState(
                            groupMessage,
                            tomP2PGroupMessage.getToUsername(),
                            MessageState.ERROR.name()
                        ),
                        keyStoreRepository.CheckSignature(
                            Username.fromString(tomP2PGroupMessage.getFromUsername()),
                            Sign.fromString(tomP2PGroupMessage.getSignature()),
                            tomP2PGroupMessage.hashCode()
                        )
                    );
                }
            });*/
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
}
