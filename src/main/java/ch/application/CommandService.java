package ch.application;

import ch.domain.CommandObject;
import ch.domain.CommandType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@AllArgsConstructor
public class CommandService {

    private Map<CommandType, Command> commands;

    public String runCommand(CommandObject commandObject) {
        return commands.get(commandObject.getCommandType())
            .run(commandObject.getValues());
    }
}
