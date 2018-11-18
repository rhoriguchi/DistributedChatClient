package ch.hsr.mapping.message;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.MessageState;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.group.GroupName;
import ch.hsr.domain.groupmessage.GroupMessage;
import ch.hsr.domain.groupmessage.GroupMessageId;
import ch.hsr.domain.keystore.Sign;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageId;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbGroup;
import ch.hsr.infrastructure.db.DbGroupMessage;
import ch.hsr.infrastructure.db.DbMessage;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessageState;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.keystore.KeyStoreRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageMapper implements MessageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageMapper.class);

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    private final PeerRepository peerRepository;
    private final GroupRepository groupRepository;
    private final KeyStoreRepository keyStoreRepository;

    public MessageMapper(DbGateway dbGateway,
                         TomP2P tomP2P,
                         PeerRepository peerRepository,
                         GroupRepository groupRepository,
                         KeyStoreRepository keyStoreRepository) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
        this.peerRepository = peerRepository;
        this.groupRepository = groupRepository;
        this.keyStoreRepository = keyStoreRepository;
    }

    @Override
    public void send(Message message) {
        DbMessage dbMessage = dbGateway.createMessage(
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.getState().name(),
            message.isValid()
        );

        try {
            tomP2P.sendMessage(messageToTomP2PMessage(message));
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            dbGateway.deleteMessage(dbMessage);
        }
    }

    private TomP2PMessage messageToTomP2PMessage(Message message) {
        return new TomP2PMessage(
            message.getId().toLong(),
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            keyStoreRepository.sign(message.hashCode()).toString(),
            TomP2PMessageState.valueOf(message.getState().name())
        );
    }

    @Override
    public void send(GroupMessage groupMessage) {
        DbGroupMessage dbGroupMessage = dbGateway.createGroupMessage(
            groupMessage.getFromPeer().getUsername().toString(),
            groupMessage.getToGroup().getId().toLong(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getStates().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> entrySet.getKey().toString(),
                    entrySet -> entrySet.getValue().name()
                )),
            groupMessage.isValid()
        );

        try {
            groupMessageToTomP2PGroupMessage(groupMessage)
                .forEach(tomP2P::sendMessage);
        } catch (IllegalArgumentException e) {
            LOGGER.error(e.getMessage(), e);
            dbGateway.deleteGroupMessage(dbGroupMessage);
        }
    }

    private Set<TomP2PGroupMessage> groupMessageToTomP2PGroupMessage(GroupMessage groupMessage) {
        return groupMessage.getStates().entrySet().stream()
            .map(entrySet -> new TomP2PGroupMessage(
                groupMessage.getId().toLong(),
                groupMessage.getFromPeer().getUsername().toString(),
                entrySet.getKey().toString(),
                groupMessage.getToGroup().getId().toLong(),
                groupMessage.getText().toString(),
                groupMessage.getTimeStamp().toString(),
                // TODO probably won't work like this
                keyStoreRepository.sign(groupMessage.hashCode()).toString(),
                TomP2PMessageState.valueOf(entrySet.getValue().name())
            )).collect(Collectors.toSet());
    }

    @Override
    public Stream<GroupMessage> getAllGroupMessages(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(DbGroup::getId)
            .map(dbGateway::getAllGroupMessages)
            .flatMap(dbGroupMessageStream -> dbGroupMessageStream.map(this::dbGroupMessageToGroupMessage));
    }

    private GroupMessage dbGroupMessageToGroupMessage(DbGroupMessage dbGroupMessage) {
        Group group = groupRepository.get(GroupId.fromLong(dbGroupMessage.getToGroupId()));

        return new GroupMessage(
            GroupMessageId.fromLong(dbGroupMessage.getId()),
            peerRepository.getPeer(Username.fromString(dbGroupMessage.getFromUsername())),
            group,
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            dbGroupMessage.getStates().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> Username.fromString(entrySet.getKey()),
                    entrySet -> MessageState.valueOf(entrySet.getValue())
                )),
            dbGroupMessage.isValid()
        );
    }

    @Override
    public void receivedMessage() {
        TomP2PMessage tomP2PMessage = tomP2P.getOldestReceivedTomP2PMessage();

        dbGateway.getMessage(tomP2PMessage.getId())
            // TODO orElseThrow?
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
            });
    }

    @Override
    public void receivedGroupMessage() {
        TomP2PGroupMessage tomP2PGroupMessage = tomP2P.getOldestReceivedTomP2PGroupMessage();

        dbGateway.getGroupMessage(tomP2PGroupMessage.getId())
            // TODO orElseThrow?
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
            });
    }

    private Map<String, String> getUpdatedDbGroupMessageState(DbGroupMessage groupMessage,
                                                              String username,
                                                              String messageState) {
        Map<String, String> states = groupMessage.getStates();
        states.replace(username, messageState);
        return states;
    }

    private Group dbGroupToGroup(DbGroup dbGroup) {
        return new Group(
            GroupId.fromLong(dbGroup.getId()),
            GroupName.fromString(dbGroup.getName()),
            dbGroup.getMembers().stream()
                .map(Username::fromString)
                .map(peerRepository::getPeer)
                .collect(Collectors.toSet())
        );
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            peerRepository.getPeer(Username.fromString(dbMessage.getFromUsername())),
            peerRepository.getPeer(Username.fromString(dbMessage.getToUsername())),
            MessageText.fromString(dbMessage.getText()),
            MessageTimeStamp.fromString(dbMessage.getTimeStamp()),
            MessageState.valueOf(dbMessage.getState()),
            dbMessage.isValid()
        );
    }

    @Override
    public Stream<Message> getAllMessages(Username ownerUsername, Username otherUsername) {
        return dbGateway.getAllMessages(ownerUsername.toString(), otherUsername.toString())
            .map(this::dbMessageToMessage);
    }
}
