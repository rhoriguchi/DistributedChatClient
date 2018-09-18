package ch.application;

import ch.application.commands.HelpCommand;
import ch.domain.CommandType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CommandConfiguration {

    @Bean
    public CommandService commandService(Map<CommandType, Command> commands) {
        return new CommandService(commands);
    }

    @Bean
    public Map<CommandType, Command> commands(HelpCommand helpCommand) {
        Map<CommandType, Command> commands = new HashMap<>();

        commands.put(CommandType.HELP, helpCommand);

        return commands;
    }

    @Bean
    public HelpCommand helpCommand() {
        return new HelpCommand();
    }
}
