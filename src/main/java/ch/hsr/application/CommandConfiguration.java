package ch.hsr.application;

import ch.hsr.application.commands.HelpCommand;
import ch.hsr.domain.CommandType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class CommandConfiguration {

    @Bean
    public CommandService commandService(Set<Command> commands) {
        return new CommandService(commands);
    }

    // TODO maybe build some kind of checker so that no CommandTypes can be assigned twice
    @Bean
    public Set<Command> commands(HelpCommand helpCommand) {
        Set<Command> commands = new HashSet<>();

        commands.add(helpCommand);

        return commands;
    }

    @Bean
    public HelpCommand helpCommand() {
        return new HelpCommand(CommandType.HELP);
    }
}
