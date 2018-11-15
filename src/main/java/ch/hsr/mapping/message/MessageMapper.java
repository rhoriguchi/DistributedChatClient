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

    public MessageMapper(DbGateway dbGateway, TomP2P tomP2P) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
    }

    @Override
    public void send(Message message) {
        DbMessage dbMessage = dbGateway.createMessage(
            message.getFromUsername().toString(),
            message.getToUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.getState().name()
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
            message.getFromUsername().toString(),
            message.getToUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            TomP2PMessageState.valueOf(message.getState().name())
        );
    }

    @Override
    public void send(GroupMessage groupMessage) {
        DbGroupMessage dbGroupMessage = dbGateway.createGroupMessage(
            groupMessage.getFromUsername().toString(),
            groupMessage.getToGroup().getId().toLong(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getStates().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> entrySet.getKey().toString(),
                    entrySet -> entrySet.getValue().name()
                ))
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
                groupMessage.getFromUsername().toString(),
                entrySet.getKey().toString(),
                groupMessage.getToGroup().getId().toLong(),
                groupMessage.getText().toString(),
                groupMessage.getTimeStamp().toString(),
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
        Group group = dbGateway.getGroup(dbGroupMessage.getToGroupId())
            .map(this::dbGroupToGroup)
            .orElseThrow(() -> new IllegalArgumentException(String.format("Group id %s does not exist",
                dbGroupMessage.getId())));

        return new GroupMessage(
            GroupMessageId.fromLong(dbGroupMessage.getId()),
            Username.fromString(dbGroupMessage.getFromUsername()),
            group,
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            dbGroupMessage.getStates().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> Username.fromString(entrySet.getKey()),
                    entrySet -> MessageState.valueOf(entrySet.getValue())
                ))
        );
    }

    @Override
    public void receivedMessage() {
        TomP2PMessage tomP2PMessage = tomP2P.getOldestReceivedMessage();

        dbGateway.getMessage(tomP2PMessage.getId())
            // TODO orElseThrow?
            .ifPresent(message -> {
                TomP2PMessageState tomP2PMessageState = TomP2PMessageState.valueOf(message.getState());

                if (message.getState().equals(MessageState.SENT.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    dbGateway.updateMessage(
                        message.getId(),
                        message.getFromUsername(),
                        message.getToUsername(),
                        message.getText(),
                        message.getTimeStamp(),
                        MessageState.RECEIVED.name()
                    );
                } else if (message.getState().equals(MessageState.RECEIVED.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    throw new IllegalArgumentException(String.format("This message %s was already received",
                        tomP2PMessage.toString()));
                } else if (tomP2PMessageState == TomP2PMessageState.ERROR) {
                    dbGateway.updateMessage(
                        message.getId(),
                        message.getFromUsername(),
                        message.getToUsername(),
                        message.getText(),
                        message.getTimeStamp(),
                        MessageState.ERROR.name()
                    );
                }
            });
    }

    @Override
    public void receivedGroupMessage() {
        TomP2PGroupMessage tomP2PGroupMessage = tomP2P.getOldestReceivedGroupMessage();

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
                        groupMessage.getId(),
                        groupMessage.getFromUsername(),
                        groupMessage.getToGroupId(),
                        groupMessage.getText(),
                        groupMessage.getTimeStamp(),
                        getUpdatedDbGroupMessageState(
                            groupMessage,
                            tomP2PGroupMessage.getToUsername(),
                            MessageState.RECEIVED.name()
                        )
                    );
                } else if (currentState.equals(MessageState.RECEIVED.name())
                    && tomP2PMessageState == TomP2PMessageState.RECEIVED) {
                    throw new IllegalArgumentException(String.format("This group message %s was already received",
                        tomP2PGroupMessage.toString()));
                } else if (tomP2PMessageState == TomP2PMessageState.ERROR) {
                    dbGateway.updateGroupMessage(
                        groupMessage.getId(),
                        groupMessage.getFromUsername(),
                        groupMessage.getToGroupId(),
                        groupMessage.getText(),
                        groupMessage.getTimeStamp(),
                        getUpdatedDbGroupMessageState(
                            groupMessage,
                            tomP2PGroupMessage.getToUsername(),
                            MessageState.ERROR.name()
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
                .collect(Collectors.toSet())
        );
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            Username.fromString(dbMessage.getFromUsername()),
            Username.fromString(dbMessage.getToUsername()),
            MessageText.fromString(dbMessage.getText()),
            MessageTimeStamp.fromString(dbMessage.getTimeStamp()),
            MessageState.valueOf(dbMessage.getState())
        );
    }

    @Override
    public Stream<Message> getAllMessages(Username ownerUsername, Username otherUsername) {
        return dbGateway.getAllMessages(ownerUsername.toString(), otherUsername.toString())
            .map(this::dbMessageToMessage);
    }
}
