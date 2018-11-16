package ch.hsr.mapping.message;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
import ch.hsr.domain.groupmessage.GroupMessage;
import ch.hsr.domain.groupmessage.GroupMessageId;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageId;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbGroup;
import ch.hsr.infrastructure.db.DbGroupMessage;
import ch.hsr.infrastructure.db.DbMessage;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.TomP2PGroupMessage;
import ch.hsr.infrastructure.tomp2p.TomP2PMessage;
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageMapper implements MessageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageMapper.class);

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    private final PeerRepository peerRepository;
    private final GroupRepository groupRepository;

    public MessageMapper(DbGateway dbGateway,
                         TomP2P tomP2P,
                         PeerRepository peerRepository,
                         GroupRepository groupRepository) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
        this.peerRepository = peerRepository;
        this.groupRepository = groupRepository;
    }

    @Override
    public void send(Message message) {
        DbMessage dbMessage = dbGateway.createMessage(
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
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
            message.getId().toLong(),
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.isReceived()
        );
    }

    @Override
    public void receivedMessage() {
        TomP2PMessage tomP2PMessage = tomP2P.getOldestReceivedTomP2PMessage();

        dbGateway.updateMessage(
            tomP2PMessage.getId(),
            tomP2PMessage.getFromUsername(),
            tomP2PMessage.getToUsername(),
            tomP2PMessage.getText(),
            tomP2PMessage.getMessageTimeStamp(),
            tomP2PMessage.isReceived()
        );
    }

    @Override
    public void send(GroupMessage groupMessage) {
        dbGateway.createGroupMessage(
            groupMessage.getFromPeer().getUsername().toString(),
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
        Group group = groupRepository.get(GroupId.fromLong(dbGroupMessage.getToGroupId()));

        return new GroupMessage(
            GroupMessageId.fromLong(dbGroupMessage.getId()),
            peerRepository.getPeer(Username.fromString(dbGroupMessage.getFromUsername())),
            group,
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            dbGroupMessage.getReceived().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> peerRepository.getPeer(Username.fromString(entrySet.getKey())),
                    Map.Entry::getValue
                ))
        );
    }

    @Override
    public void receivedGroupMessage() {
        TomP2PGroupMessage tomP2PGroupMessage = tomP2P.getOldestReceivedTomP2PGroupMessage();

        dbGateway.updateGroupMessage(
            tomP2PGroupMessage.getId(),
            tomP2PGroupMessage.getFromUsername(),
            tomP2PGroupMessage.getToGroupId(),
            tomP2PGroupMessage.getText(),
            tomP2PGroupMessage.getTimeStamp(),
            tomP2PGroupMessage.getReceived().entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue
                ))
        );
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            peerRepository.getPeer(Username.fromString(dbMessage.getFromUsername())),
            peerRepository.getPeer(Username.fromString(dbMessage.getToUsername())),
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
