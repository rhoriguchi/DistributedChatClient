package ch.hsr.application;

import ch.hsr.domain.common.PeerId;
import ch.hsr.domain.common.Username;
import ch.hsr.domain.message.Message;
import ch.hsr.domain.message.MessageText;
import ch.hsr.mapping.message.MessageRepository;
import ch.hsr.mapping.peer.PeerRepository;
import java.util.stream.Stream;

public class MessageService {

    private final MessageRepository messageRepository;
    private final PeerRepository peerRepository;

    public MessageService(MessageRepository messageRepository, PeerRepository peerRepository) {
        this.messageRepository = messageRepository;
        this.peerRepository = peerRepository;
    }

    public void send(Username toUsername, MessageText messageText) {
        PeerId fromId = peerRepository.getSelf().getPeerId();
        PeerId toId = peerRepository.getPeerId(toUsername);

        if (!fromId.equals(toId)) {
            messageRepository.send(Message.newMessage(
                fromId,
                toId,
                messageText
            ));
        } else {
            throw new IllegalArgumentException("Messages can't be sent to yourself");
        }
    }

    public Stream<Message> getAllMessages(PeerId otherId) {
        PeerId ownerId = peerRepository.getSelf().getPeerId();

        return messageRepository.getAll(ownerId, otherId);
    }

}
