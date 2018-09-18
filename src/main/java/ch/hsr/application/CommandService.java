package ch.hsr.application;

import ch.hsr.domain.CommandObject;
import ch.hsr.domain.CommandType;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class CommandService {

    private Map<CommandType, Command> commands;

    public CommandService(Map<CommandType, Command> commands) {
        this.commands = commands;
    }

    public String executeCommand(CommandObject commandObject) {
        Command command = commands.get(commandObject.getCommandType());

        command.checkValues(commandObject.getValues());

        return command.execute(commandObject.getValues());
    }
}
