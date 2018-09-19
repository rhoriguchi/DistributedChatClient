package ch.hsr.application;

import ch.hsr.domain.CommandObject;
import ch.hsr.domain.CommandType;
import org.springframework.stereotype.Service;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import java.util.Map;

@Service
public class CommandService {

    private Map<CommandType, Command> commands;

    public CommandService(Map<CommandType, Command> commands) {
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
        if (commands.containsKey(commandObject.getCommandType())) {
            return commands.get(commandObject.getCommandType());
        } else {
            throw new NotImplementedException();
        }
    }
}
