package ch.hsr.mapping.message;

import ch.hsr.domain.common.GroupId;
import ch.hsr.domain.common.MessageState;
import ch.hsr.domain.common.MessageText;
import ch.hsr.domain.common.MessageTimeStamp;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.group.Group;
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
import ch.hsr.mapping.group.GroupRepository;
import ch.hsr.mapping.keystore.KeyStoreRepository;
import ch.hsr.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            message.getState().name()
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
            .map(entrySet -> entrySet.getKey().getUsername())
            .map(username -> new TomP2PGroupMessage(
                groupMessage.getId().toLong(),
                groupMessage.getFromPeer().getUsername().toString(),
                username.toString(),
                groupMessage.getToGroup().getId().toLong(),
                groupMessage.getText().toString(),
                groupMessage.getTimeStamp().toString(),
                // TODO probably won't work like this
                keyStoreRepository.sign(groupMessage.hashCode()).toString(),
                groupMessage.getStates().entrySet().stream()
                    .collect(Collectors.toMap(
                        entrySet -> entrySet.getKey().getUsername().toString(),
                        entrySet -> entrySet.getValue().name()
                    ))
            )).collect(Collectors.toSet());
    }

    @Override
    public Stream<GroupMessage> getAllGroupMessages(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(DbGroup::getId)
            .map(dbGateway::getAllGroupMessages)
            .flatMap(dbGroupMessageStream -> dbGroupMessageStream.map(this::dbGroupMessageToGroupMessage));
    }

    @Override
    public GroupMessage getGroupMessage(GroupMessageId groupMessageId) {
        return dbGateway.getGroupMessage(groupMessageId.toLong())
            .map(this::dbGroupMessageToGroupMessage)
            // TODO wrong exception
            .orElseThrow(() -> new IllegalArgumentException(String.format("Group message with id %s does not exist",
                groupMessageId.toString())));
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
                    entrySet -> peerRepository.getPeer(Username.fromString(entrySet.getKey())),
                    entrySet -> MessageState.valueOf(entrySet.getValue())
                )),
            dbGroupMessage.isValid()
        );
    }

    @Override
    public Message receivedMessage() {
        return tomP2PMessageToMessage(tomP2P.getOldestReceivedTomP2PMessage());
    }

    private Message tomP2PMessageToMessage(TomP2PMessage tomP2PMessage) {
        return new Message(
            MessageId.fromLong(tomP2PMessage.getId()),
            peerRepository.getPeer(Username.fromString(tomP2PMessage.getFromUsername())),
            peerRepository.getPeer(Username.fromString(tomP2PMessage.getToUsername())),
            MessageText.fromString(tomP2PMessage.getText()),
            MessageTimeStamp.fromString(tomP2PMessage.getTimeStamp()),
            MessageState.valueOf(tomP2PMessage.getState()),
            keyStoreRepository.CheckSignature(
                Username.fromString(tomP2PMessage.getFromUsername()),
                Sign.fromString(tomP2PMessage.getSignature()),
                tomP2PMessage.hashCode()
            )
        );
    }

    @Override
    public GroupMessage receivedGroupMessage() {
        return tomP2PGroupMessageToGroupMessage(tomP2P.getOldestReceivedTomP2PGroupMessage());
    }

    private GroupMessage tomP2PGroupMessageToGroupMessage(TomP2PGroupMessage tomP2PGroupMessage) {
        return new GroupMessage(
            GroupMessageId.fromLong(tomP2PGroupMessage.getId()),
            peerRepository.getPeer(Username.fromString(tomP2PGroupMessage.getFromUsername())),
            groupRepository.get(GroupId.fromLong(tomP2PGroupMessage.getToGroupId())),
            MessageText.fromString(tomP2PGroupMessage.getText()),
            MessageTimeStamp.fromString(tomP2PGroupMessage.getTimeStamp()),
            tomP2PGroupMessage.getStates().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> peerRepository.getPeer(Username.fromString(entrySet.getKey())),
                    entrySet -> {
                        if (entrySet.getKey().equals(peerRepository.getSelf().getUsername().toString())) {
                            return MessageState.valueOf(entrySet.getValue());
                        } else {
                            return MessageState.UNKNOWN;
                        }
                    }
                )),
            // TODO probably won't work like this
            keyStoreRepository.CheckSignature(
                Username.fromString(tomP2PGroupMessage.getFromUsername()),
                Sign.fromString(tomP2PGroupMessage.getSignature()),
                tomP2PGroupMessage.hashCode()
            )
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

    @Override
    public Message getMessage(MessageId messageId) {
        return dbGateway.getMessage(messageId.toLong())
            .map(this::dbMessageToMessage)
            // TODO wrong exception
            .orElseThrow(() -> new IllegalArgumentException(String.format("Message with id %s does not exist",
                messageId.toString())));
    }
}
