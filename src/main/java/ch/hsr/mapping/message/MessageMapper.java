package ch.hsr.mapping.message;

import ch.hsr.domain.common.PeerId;
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
        // TODO send message over tomP2P
        return dbMessageToMessage(dbMessage);
    }

    private Message dbMessageToMessage(DbMessage dbMessage) {
        return new Message(
            MessageId.fromLong(dbMessage.getId()),
            PeerId.fromString(dbMessage.getFromId()),
            PeerId.fromString(dbMessage.getToId()),
            MessageText.fromString(dbMessage.getText()),
            MessageTimeStamp.fromString(dbMessage.getTimeStamp())
        );
    }

    private DbMessage messageToDbMessage(Message message) {
        return new DbMessage(
            message.getFromId().toString(),
            message.getToId().toString(),
            message.getText().toString(),
            message.getMessageTimeStamp().toString()
        );
    }

    @Override
    public Stream<Message> getAll(PeerId ownerId, PeerId otherId) {
        return dbGateway.getAllMessages(ownerId.toString(), otherId.toString())
            .map(this::dbMessageToMessage);
    }
}
