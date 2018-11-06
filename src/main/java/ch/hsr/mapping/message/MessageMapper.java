package ch.hsr.mapping.message;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageId;
import ch.hsr.domain.message.MessageText;
import ch.hsr.domain.message.MessageTimeStamp;
import ch.hsr.infrastructure.db.DbGateway;
import ch.hsr.infrastructure.db.DbMessage;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import java.util.stream.Stream;

public class MessageMapper implements MessageRepository {

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    public MessageMapper(DbGateway dbGateway, TomP2P tomP2P) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
    }

    @Override
    public Message send(Message message) {
        DbMessage dbMessage = dbGateway.createMessage(messageToDbMessage(message));
        tomP2P.sendMessage(message.getFromUsername().toString(), message.getText().toString());
        return dbMessageToMessage(dbMessage);
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            Username.fromString(dbMessage.getFromUsername()),
            Username.fromString(dbMessage.getToUsername()),
            MessageText.fromString(dbMessage.getText()),
            MessageTimeStamp.fromString(dbMessage.getTimeStamp())
        );
    }

    private DbMessage messageToDbMessage(Message message) {
        return new DbMessage(
            message.getFromUsername().toString(),
            message.getToUsername().toString(),
            message.getText().toString(),
            message.getMessageTimeStamp().toString()
        );
    }

    @Override
    public Stream<Message> getAll(Username ownerUsername, Username otherUsername) {
        return dbGateway.getAllMessages(ownerUsername.toString(), otherUsername.toString())
            .map(this::dbMessageToMessage);
    }
}
