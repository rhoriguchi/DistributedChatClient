package ch.hsr.application;

import ch.hsr.domain.CommandObject;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Set;

@Service
public class CommandService {

    private Set<Command> commands;

    public CommandService(Set<Command> commands) {
        this.commands = commands;
    }

    public String executeCommand(CommandObject commandObject) {
        Command command = getCommand(commandObject);
        return executeCommand(command, commandObject);
    }

    private String executeCommand(Command command, CommandObject commandObject) {
        checkCommand(command, commandObject);
        return command.execute(commandObject.getValues());
    }

    private void checkCommand(Command command, CommandObject commandObject) {
        command.checkValues(commandObject.getValues());
    }

    private Command getCommand(CommandObject commandObject) {
        return commands.stream()
            .filter(command -> command.getCommandType() == command.getCommandType())
            .findFirst()
            .orElseThrow(NotImplementedException::new);
    }
}
