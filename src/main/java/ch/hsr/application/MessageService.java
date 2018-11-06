package ch.hsr.application;

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

    public void sendMessage(Username toUsername, MessageText message) {
        Username fromUsername = peerRepository.getSelf().getUsername();

        if (!fromUsername.equals(toUsername)) {
            messageRepository.send(Message.newMessage(
                fromUsername,
                toUsername,
                message
            ));
        } else {
            throw new IllegalArgumentException("Messages can't be sent to yourself");
        }
    }

    public Stream<Message> getAllMessages(Username username) {
        Username ownerUsername = peerRepository.getSelf().getUsername();

        return messageRepository.getAll(ownerUsername, username);
    }

}
