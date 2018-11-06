package ch.hsr.mapping.message;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageId;
import ch.hsr.domain.message.MessageText;
import ch.hsr.domain.message.MessageTimeStamp;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbMessage;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.infrastructure.tomp2p.TomP2PMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        DbMessage dbMessage = dbGateway.createMessage(messageToDbMessage(message));

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
            message.getMessageTimeStamp().toString(),
            message.getReceived()
        );
    }

    private DbMessage messageToDbMessage(Message message) {
        return DbMessage.newDbMessage(
            message.getFromUsername().toString(),
            message.getToUsername().toString(),
            message.getText().toString(),
            message.getMessageTimeStamp().toString(),
            message.getReceived()
        );
    }

    @Override
    public void received() {
        Message message = tomP2PMessageToMessage(tomP2P.getOldestReceivedTomP2PMessage());

        dbGateway.createMessage(messageToDbMessage(message));
    }

    private Message tomP2PMessageToMessage(TomP2PMessage tomP2PMessage) {
        return new Message(
            MessageId.fromLong(tomP2PMessage.getId()),
            Username.fromString(tomP2PMessage.getFromUsername()),
            Username.fromString(tomP2PMessage.getToUsername()),
            MessageText.fromString(tomP2PMessage.getText()),
            MessageTimeStamp.fromString(tomP2PMessage.getMessageTimeStamp()),
            tomP2PMessage.getReceived()
        );
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            Username.fromString(dbMessage.getFromUsername()),
            Username.fromString(dbMessage.getToUsername()),
            MessageText.fromString(dbMessage.getText()),
            MessageTimeStamp.fromString(dbMessage.getTimeStamp()),
            dbMessage.getReceived()
        );
    }

    @Override
    public Stream<Message> getAll(Username ownerUsername, Username otherUsername) {
        return dbGateway.getAllMessages(ownerUsername.toString(), otherUsername.toString())
            .map(this::dbMessageToMessage);
    }
}
