package ch.hsr.dcc.infrastructure.tomp2p.message;

import ch.hsr.dcc.infrastructure.tomp2p.PeerHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageHandlerConfiguration {

    @Bean
    public MessageHandler messageHandler(PeerHolder peerHolder,
                                               MessageQueHolder messageQueHolder) {
        return new MessageHandler(peerHolder, messageQueHolder);
    }
}
