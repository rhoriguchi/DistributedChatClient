package ch.hsr.dsa.mapping.message;

import ch.hsr.dsa.domain.common.GroupId;
import ch.hsr.dsa.domain.common.MessageText;
import ch.hsr.dsa.domain.common.MessageTimeStamp;
import ch.hsr.dsa.domain.common.Username;
import ch.hsr.dsa.domain.groupmessage.GroupMessage;
import ch.hsr.dsa.domain.groupmessage.GroupMessageId;
import ch.hsr.dsa.domain.keystore.Sign;
import ch.hsr.dsa.domain.keystore.SignState;
import ch.hsr.dsa.domain.message.Message;
import ch.hsr.dsa.domain.message.MessageId;
import ch.hsr.dsa.domain.peer.Peer;
import ch.hsr.dsa.infrastructure.db.DbGateway;
import ch.hsr.dsa.infrastructure.db.DbGroup;
import ch.hsr.dsa.infrastructure.db.DbGroupMessage;
import ch.hsr.dsa.infrastructure.db.DbMessage;
import ch.hsr.dsa.infrastructure.tomp2p.TomP2P;
import ch.hsr.dsa.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dsa.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dsa.mapping.Util.TomP2PPeerAddressHelper;
import ch.hsr.dsa.mapping.group.GroupRepository;
import ch.hsr.dsa.mapping.keystore.KeyStoreRepository;
import ch.hsr.dsa.mapping.peer.PeerRepository;
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

        if (peer.isOnline()) {
            DbMessage dbMessage = dbGateway.saveMessage(newDbMessage(message));

            try {
                tomP2P.sendMessage(dbMessageToTomP2PMessage(dbMessage),
                    TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));

                //TODO to broad exception
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);

                dbGateway.getMessage(dbMessage.getId())
                    //TODO bad name
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
        return new TomP2PMessage(
            dbMessage.getFromUsername(),
            dbMessage.getToUsername(),
            dbMessage.getText(),
            dbMessage.getTimeStamp(),
            keyStoreRepository.sign(dbMessage.hashCode()).toString()
        );
    }

    private DbMessage newDbMessage(Message message) {
        return DbMessage.newDbMessage(
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString(),
            message.getSignState().name(),
            message.isFailed()
        );
    }

    @Override
    public void createMessage(Message message) {
        dbGateway.saveMessage(newDbMessage(message));
    }

    // TODO if all fail don't save
    @Override
    public void send(GroupMessage groupMessage) {
        DbGroupMessage dbGroupMessage = dbGateway.saveGroupMessage(newDbGroupMessage(groupMessage));

        groupMessage.getToPeers().stream()
            .map(Peer::getUsername)
            .forEach(username -> {
                Peer peer = peerRepository.get(username);

                if (peer.isOnline()) {
                    try {
                        tomP2P.sendMessage(dbGroupMessageToTomP2PGroupMessage(dbGroupMessage, username),
                            TomP2PPeerAddressHelper.getTomP2PPeerAddress(peer));

                        //TODO to broad exception
                    } catch (Exception e) {
                        LOGGER.error(e.getMessage(), e);

                        dbGateway.getGroupMessage(dbGroupMessage.getId())
                            //TODO bad name
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
        return new TomP2PGroupMessage(
            dbGroupMessage.getGroupId(),
            dbGroupMessage.getFromUsername(),
            username.toString(),
            dbGroupMessage.getText(),
            dbGroupMessage.getTimeStamp(),
            keyStoreRepository.sign(dbGroupMessage.hashCode()).toString()
        );
    }

    private DbGroupMessage newDbGroupMessage(GroupMessage groupMessage) {
        return DbGroupMessage.newDbGroupMessage(
            groupMessage.getGroup().getId().toLong(),
            groupMessage.getFromPeer().getUsername().toString(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString(),
            groupMessage.getSignState().name(),
            groupMessage.getFailed().entrySet().stream()
                .collect(Collectors.toMap(
                    entrySet -> entrySet.getKey().getUsername().toString(),
                    Map.Entry::getValue
                ))
        );
    }

    @Override
    public void createGroupMessage(GroupMessage groupMessage) {
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
            groupRepository.get(GroupId.fromLong(dbGroupMessage.getGroupId())),
            peerRepository.get(Username.fromString(dbGroupMessage.getFromUsername())),
            MessageText.fromString(dbGroupMessage.getText()),
            MessageTimeStamp.fromString(dbGroupMessage.getTimeStamp()),
            SignState.valueOf(dbGroupMessage.getSignState()),
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
            keyStoreRepository.CheckSignature(
                Username.fromString(tomP2PMessage.getFromUsername()),
                Sign.fromString(tomP2PMessage.getSignature()),
                tomP2PMessage.hashCode()
            ),
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
            groupRepository.get(GroupId.fromLong(tomP2PGroupMessage.getToGroupId())),
            peerRepository.get(Username.fromString(tomP2PGroupMessage.getFromUsername())),
            MessageText.fromString(tomP2PGroupMessage.getText()),
            MessageTimeStamp.fromString(tomP2PGroupMessage.getTimeStamp()),
            keyStoreRepository.CheckSignature(
                Username.fromString(tomP2PGroupMessage.getFromUsername()),
                Sign.fromString(tomP2PGroupMessage.getSignature()),
                tomP2PGroupMessage.hashCode()
            ),
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
            SignState.valueOf(dbMessage.getSignState()),
            dbMessage.isFailed()
        );
    }

    @Override
    public Optional<Message> getMessage(MessageId messageId) {
        return dbGateway.getMessage(messageId.toLong())
            .map(this::dbMessageToMessage);
    }
}
