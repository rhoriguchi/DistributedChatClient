package ch.presentation.commandLine;

import ch.application.CommandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConsoleListenerConfiguration {

    @Bean
    public ConsoleListener consoleListener(CommandService commandService) {
        return new ConsoleListener(commandService);
    }
}
