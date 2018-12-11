package ch.hsr.dcc.mapping.message;

import ch.hsr.dcc.domain.common.MessageText;
import ch.hsr.dcc.domain.common.MessageTimeStamp;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.group.GroupId;
import ch.hsr.dcc.domain.groupmessage.GroupMessage;
import ch.hsr.dcc.domain.groupmessage.GroupMessageId;
import ch.hsr.dcc.domain.message.Message;
import ch.hsr.dcc.domain.message.MessageId;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.db.DbGroup;
import ch.hsr.dcc.infrastructure.db.DbGroupMessage;
import ch.hsr.dcc.infrastructure.db.DbMessage;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dcc.mapping.Util.TomP2PPeerAddressHelper;
import ch.hsr.dcc.mapping.exception.MessageException;
import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.notary.NotaryRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessageMapper implements MessageRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageMapper.class);

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    private final PeerRepository peerRepository;
    private final GroupRepository groupRepository;
    private final NotaryRepository notaryRepository;

    public MessageMapper(DbGateway dbGateway,
                         TomP2P tomP2P,
                         PeerRepository peerRepository,
                         GroupRepository groupRepository,
                         NotaryRepository notaryRepository) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
        this.peerRepository = peerRepository;
        this.groupRepository = groupRepository;
        this.notaryRepository = notaryRepository;
    }

    @Override
    public void send(Message message) {
        Peer peer = peerRepository.get(message.getToPeer().getUsername());

        //TODO do this in serviceLayer
        if (peer.isOnline()) {
            DbMessage dbMessage = dbGateway.saveMessage(newDbMessage(message));

            try {
                TomP2PMessage tomP2PMessage = dbMessageToTomP2PMessage(dbMessage);

                notaryRepository.notarize(tomP2PMessage);
                tomP2P.sendMessage(tomP2PMessage, TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));

                //TODO to broad exception
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);

                dbGateway.getMessage(dbMessage.getId())
                    .ifPresent(dbMessage1 -> {
                        dbMessage1.setFailed(true);
                        dbGateway.saveMessage(dbMessage);
                    });
            }
        } else {
            throw new MessageException(String.format("Peer %s is offline", peer.getUsername()));
        }
    }

    private TomP2PMessage dbMessageToTomP2PMessage(DbMessage dbMessage) {
        return new TomP2PMessage(
            dbMessage.getFromUsername(),
            dbMessage.getToUsername(),
            dbMessage.getText(),
            dbMessage.getTimeStamp()
        );
    }

    private DbMessage newDbMessage(Message message) {
        return DbMessage.newDbMessage(
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.isFailed()
        );
    }

    @Override
    public void saveMessage(Message message) {
        dbGateway.saveMessage(newDbMessage(message));
    }

    @Override
    public void send(GroupMessage groupMessage) {
        DbGroupMessage dbGroupMessage = dbGateway.saveGroupMessage(newDbGroupMessage(groupMessage));

        groupMessage.getToPeers().stream()
            .map(Peer::getUsername)
            .forEach(username -> {
                Peer peer = peerRepository.get(username);

                //TODO do this in serviceLayer
                if (peer.isOnline()) {
                    try {
                        TomP2PGroupMessage tomP2PGroupMessage = dbGroupMessageToTomP2PGroupMessage(dbGroupMessage,
                            username);

                        notaryRepository.notarize(tomP2PGroupMessage);
                        tomP2P.sendMessage(tomP2PGroupMessage, TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));

                        //TODO to broad exception
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);

                        dbGateway.getGroupMessage(dbGroupMessage.getId())
                            .ifPresent(dbGroupMessage1 -> {
                                Map<String, Boolean> failed = dbGroupMessage.getFailed();
                                failed.put(username.toString(), true);
                                dbGroupMessage.setFailed(failed);
                                dbGateway.saveGroupMessage(dbGroupMessage);
                            });
                    }
                } else {
                    throw new MessageException(String.format("Peer %s is offline", peer.getUsername()));
                }
            });
    }

    private TomP2PGroupMessage dbGroupMessageToTomP2PGroupMessage(DbGroupMessage dbGroupMessage, Username username) {
        return new TomP2PGroupMessage(
            dbGroupMessage.getGroupId(),
            dbGroupMessage.getFromUsername(),
            username.toString(),
            dbGroupMessage.getText(),
            dbGroupMessage.getTimeStamp()
        );
    }

    private DbGroupMessage newDbGroupMessage(GroupMessage groupMessage) {
        return DbGroupMessage.newDbGroupMessage(
            groupMessage.getGroup().getId().toLong(),
            groupMessage.getFromPeer().getUsername().toString(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getFailed().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> entrySet.getKey().getUsername().toString(),
                    Map.Entry::getValue
                ))
        );
    }

    @Override
    public void saveGroupMessage(GroupMessage groupMessage) {
        dbGateway.saveGroupMessage(newDbGroupMessage(groupMessage));
    }

    @Override
    public Stream<GroupMessage> getAllGroupMessages(Username username) {
        return dbGateway.getAllGroups(username.toString())
            .map(DbGroup::getId)
            .map(dbGateway::getAllGroupMessages)
            .flatMap(dbGroupMessageStream -> dbGroupMessageStream.map(this::dbGroupMessageToGroupMessage));
    }

    private GroupMessage dbGroupMessageToGroupMessage(DbGroupMessage dbGroupMessage) {
        GroupId groupId = GroupId.fromLong(dbGroupMessage.getId());
        return new GroupMessage(
            GroupMessageId.fromLong(dbGroupMessage.getId()),
            groupRepository.get(groupId)
                .orElseGet(() -> Group.empty(groupId)),
            peerRepository.get(Username.fromString(dbGroupMessage.getFromUsername())),
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            dbGroupMessage.getFailed().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> peerRepository.get(Username.fromString(entrySet.getKey())),
                    Map.Entry::getValue
                ))
        );
    }

    @Override
    public Optional<GroupMessage> getGroupMessage(GroupMessageId groupMessageId) {
        return dbGateway.getGroupMessage(groupMessageId.toLong())
            .map(this::dbGroupMessageToGroupMessage);
    }

    @Override
    public Message oldestReceivedMessage() {
        return tomP2PMessageToMessage(tomP2P.getOldestReceivedTomP2PMessage());
    }

    private Message tomP2PMessageToMessage(TomP2PMessage tomP2PMessage) {
        return new Message(
            MessageId.empty(),
            peerRepository.get(Username.fromString(tomP2PMessage.getFromUsername())),
            peerRepository.get(Username.fromString(tomP2PMessage.getToUsername())),
            MessageText.fromString(tomP2PMessage.getText()),
            MessageTimeStamp.fromString(tomP2PMessage.getTimeStamp()),
            false
        );
    }

    @Override
    public GroupMessage oldestReceivedGroupMessage() {
        return tomP2PGroupMessageToGroupMessage(tomP2P.getOldestReceivedTomP2PGroupMessage());
    }

    private GroupMessage tomP2PGroupMessageToGroupMessage(TomP2PGroupMessage tomP2PGroupMessage) {
        Map<Peer, Boolean> failed = new HashMap<>();
        failed.put(
            peerRepository.get(Username.fromString(tomP2PGroupMessage.getToUsername())),
            false
        );

        GroupId groupId = GroupId.fromLong(tomP2PGroupMessage.getToGroupId());
        return new GroupMessage(
            GroupMessageId.empty(),
            //TODO duplicate
            groupRepository.get(groupId)
                .orElseGet(() -> Group.empty(groupId)),
            peerRepository.get(Username.fromString(tomP2PGroupMessage.getFromUsername())),
            MessageText.fromString(tomP2PGroupMessage.getText()),
            MessageTimeStamp.fromString(tomP2PGroupMessage.getTimeStamp()),
            failed
        );
    }

    @Override
    public Stream<Message> getAllMessages(Username ownerUsername, Username otherUsername) {
        return dbGateway.getAllMessages(ownerUsername.toString(), otherUsername.toString())
            .map(this::dbMessageToMessage);
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            peerRepository.get(Username.fromString(dbMessage.getFromUsername())),
            peerRepository.get(Username.fromString(dbMessage.getToUsername())),
            MessageText.fromString(dbMessage.getText()),
            MessageTimeStamp.fromString(dbMessage.getTimeStamp()),
            dbMessage.isFailed()
        );
    }

    @Override
    public Optional<Message> getMessage(MessageId messageId) {
        return dbGateway.getMessage(messageId.toLong())
            .map(this::dbMessageToMessage);
    }
}
