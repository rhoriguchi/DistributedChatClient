package ch.hsr.dcc.mapping.message;

import ch.hsr.dcc.domain.common.GroupId;
import ch.hsr.dcc.domain.common.MessageText;
import ch.hsr.dcc.domain.common.MessageTimeStamp;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.group.GroupChangedTimeStamp;
import ch.hsr.dcc.domain.group.GroupName;
import ch.hsr.dcc.domain.groupmessage.GroupMessage;
import ch.hsr.dcc.domain.groupmessage.GroupMessageId;
import ch.hsr.dcc.domain.keystore.Sign;
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
import ch.hsr.dcc.mapping.group.GroupRepository;
import ch.hsr.dcc.mapping.keystore.KeyStoreRepository;
import ch.hsr.dcc.mapping.peer.PeerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.HashMap;
import java.util.HashSet;
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
        Peer peer = peerRepository.get(message.getToPeer().getUsername());

        //TODO do this in serviceLayer
        if (peer.isOnline()) {
            DbMessage dbMessage = dbGateway.saveMessage(newDbMessage(message));

            try {
                tomP2P.sendMessage(dbMessageToTomP2PMessage(dbMessage),
                    TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));

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
            //TODO wrong exception
            throw new IllegalArgumentException(String.format("Peer %s is offline", peer.getUsername()));
        }
    }

    private TomP2PMessage dbMessageToTomP2PMessage(DbMessage dbMessage) {
        //TODO use new function
        TomP2PMessage tomP2PMessage = new TomP2PMessage(
            dbMessage.getFromUsername(),
            dbMessage.getToUsername(),
            dbMessage.getText(),
            dbMessage.getTimeStamp(),
            null
        );

        tomP2PMessage.setSignature(keyStoreRepository.sign(tomP2PMessage).toString());

        return tomP2PMessage;
    }

    private DbMessage newDbMessage(Message message) {
        return DbMessage.newDbMessage(
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.getSign().toString(),
            message.isFailed()
        );
    }

    @Override
    public void saveMessage(Message message) {
        dbGateway.saveMessage(newDbMessage(message));
    }

    //TODO if all fail don't save
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
                        tomP2P.sendMessage(dbGroupMessageToTomP2PGroupMessage(dbGroupMessage, username),
                            TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));

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
                    //TODO wrong exception
                    throw new IllegalArgumentException(String.format("Peer %s is offline", peer.getUsername()));
                }
            });
    }

    private TomP2PMessage dbGroupMessageToTomP2PGroupMessage(DbGroupMessage dbGroupMessage, Username username) {
        //TODO use new function
        TomP2PGroupMessage tomP2PGroupMessage = new TomP2PGroupMessage(
            dbGroupMessage.getGroupId(),
            dbGroupMessage.getFromUsername(),
            username.toString(),
            dbGroupMessage.getText(),
            dbGroupMessage.getTimeStamp(),
            null
        );

        tomP2PGroupMessage.setSignature(keyStoreRepository.sign(tomP2PGroupMessage).toString());

        return tomP2PGroupMessage;
    }

    private DbGroupMessage newDbGroupMessage(GroupMessage groupMessage) {
        return DbGroupMessage.newDbGroupMessage(
            groupMessage.getGroup().getId().toLong(),
            groupMessage.getFromPeer().getUsername().toString(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getSign().toString(),
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
        return new GroupMessage(
            GroupMessageId.fromLong(dbGroupMessage.getId()),
            //TODO duplicate
            groupRepository.get(GroupId.fromLong(dbGroupMessage.getGroupId()))
                .orElseGet(() -> new Group(
                    GroupId.fromLong(dbGroupMessage.getGroupId()),
                    GroupName.empty(),
                    Peer.empty(),
                    new HashSet<>(),
                    GroupChangedTimeStamp.empty(),
                    Sign.empty()
                )),
            peerRepository.get(Username.fromString(dbGroupMessage.getFromUsername())),
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            Sign.fromString(dbGroupMessage.getSignature()),
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
            Sign.fromString(tomP2PMessage.getSignature()),
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

        return new GroupMessage(
            GroupMessageId.empty(),
            //TODO duplicate
            groupRepository.get(GroupId.fromLong(tomP2PGroupMessage.getToGroupId()))
                .orElseGet(() -> new Group(
                    GroupId.fromLong(tomP2PGroupMessage.getToGroupId()),
                    GroupName.empty(),
                    Peer.empty(),
                    new HashSet<>(),
                    GroupChangedTimeStamp.empty(),
                    Sign.empty()
                )),
            peerRepository.get(Username.fromString(tomP2PGroupMessage.getFromUsername())),
            MessageText.fromString(tomP2PGroupMessage.getText()),
            MessageTimeStamp.fromString(tomP2PGroupMessage.getTimeStamp()),
            Sign.fromString(tomP2PGroupMessage.getSignature()),
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
            Sign.fromString(dbMessage.getSignature()),
            dbMessage.isFailed()
        );
    }

    @Override
    public Optional<Message> getMessage(MessageId messageId) {
        return dbGateway.getMessage(messageId.toLong())
            .map(this::dbMessageToMessage);
    }
}
