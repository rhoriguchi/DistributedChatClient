package ch.hsr.application;

import ch.hsr.domain.message.Message;
import ch.hsr.domain.user.Username;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class MessageService {

    public MessageService() {

    }

    public void send(Message message) {
        // TODO send message
    }
}
