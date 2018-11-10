package ch.hsr.mapping.message;

import ch.hsr.domain.common.GroupId;
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
import ch.hsr.infrastructure.tomp2p.message.TomP2PDefaultMessageState;
import ch.hsr.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.message.TomP2PMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
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
            message.isReceived()
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
            TomP2PDefaultMessageState.SENT,
            message.getId().toLong(),
            message.getFromUsername().toString(),
            message.getToUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.isReceived()
        );
    }

    @Override
    public void receivedMessage() {
        Message message = tomP2PMessageToMessage(tomP2P.getOldestReceivedTomP2PMessage());

        dbGateway.updateMessage(
            message.getId().toLong(),
            message.getFromUsername().toString(),
            message.getToUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.isReceived()
        );
    }

    private Message tomP2PMessageToMessage(TomP2PMessage tomP2PMessage) {
        return new Message(
            MessageId.fromLong(tomP2PMessage.getId()),
            Username.fromString(tomP2PMessage.getFromUsername()),
            Username.fromString(tomP2PMessage.getToUsername()),
            MessageText.fromString(tomP2PMessage.getText()),
            MessageTimeStamp.fromString(tomP2PMessage.getMessageTimeStamp()),
            tomP2PMessage.isReceived()
        );
    }

    @Override
    public void send(GroupMessage groupMessage) {
        dbGateway.createGroupMessage(
            groupMessage.getFromUsername().toString(),
            groupMessage.getToGroup().getId().toLong(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getReceived().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> entrySet.getKey().toString(),
                    Map.Entry::getValue
                ))
        );
    }

    @Override
    public Stream<GroupMessage> getAllGroupMessages(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(DbGroup::getId)
            .map(dbGateway::getAllGroupMessages)
            .flatMap(dbGroupMessageStream -> dbGroupMessageStream.map(this::dbGroupMessageToGroupMessage));
    }

    private GroupMessage dbGroupMessageToGroupMessage(DbGroupMessage dbGroupMessage) {
        // TODO duplicate
        Group group = dbGateway.getGroup(dbGroupMessage.getToGroupId())
            .map(this::dbGroupToGroup)
            // TODO good solution?
            .orElseGet(Group::empty);

        return new GroupMessage(
            GroupMessageId.fromLong(dbGroupMessage.getId()),
            Username.fromString(dbGroupMessage.getFromUsername()),
            group,
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            dbGroupMessage.getReceived().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> Username.fromString(entrySet.getKey()),
                    Map.Entry::getValue
                ))
        );
    }

    @Override
    public void receivedGroupMessage() {
        GroupMessage groupMessage = tomP2PMessageToGroupMessage(tomP2P.getOldestReceivedTomP2PGroupMessage());

        dbGateway.updateGroupMessage(
            groupMessage.getId().toLong(),
            groupMessage.getFromUsername().toString(),
            groupMessage.getToGroup().getId().toLong(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getReceived().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> entrySet.getKey().toString(),
                    Map.Entry::getValue
                ))
        );
    }

    private GroupMessage tomP2PMessageToGroupMessage(TomP2PGroupMessage tomP2PGroupMessage) {
        // TODO duplicate
        Group group = dbGateway.getGroup(tomP2PGroupMessage.getToGroupId())
            .map(this::dbGroupToGroup)
            // TODO good solution?
            .orElseGet(Group::empty);

        return new GroupMessage(
            GroupMessageId.fromLong(tomP2PGroupMessage.getId()),
            Username.fromString(tomP2PGroupMessage.getFromUsername()),
            group,
            MessageText.fromString(tomP2PGroupMessage.getText()),
            MessageTimeStamp.fromString(tomP2PGroupMessage.getTimeStamp()),
            tomP2PGroupMessage.getReceived().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> Username.fromString(entrySet.getKey()),
                    Map.Entry::getValue
                ))
        );
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
            dbMessage.isReceived()
        );
    }

    @Override
    public Stream<Message> getAllMessages(Username ownerUsername, Username otherUsername) {
        return dbGateway.getAllMessages(ownerUsername.toString(), otherUsername.toString())
            .map(this::dbMessageToMessage);
    }
}
