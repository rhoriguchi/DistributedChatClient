package ch.hsr.application;

import ch.hsr.domain.CommandType;
import ch.hsr.domain.CommandVariableType;
import lombok.Getter;
import java.util.Map;

@Getter
public abstract class Command {

    private CommandType commandType;

    public Command(CommandType commandType) {
        this.commandType = commandType;
    }

    void checkValues(Map<CommandVariableType, String> values) throws IllegalArgumentException {
        if (commandType.getVariables().size() != values.size()) {
            throw new IllegalArgumentException(String.format("Only %s values allowed for %s but %s used",
                commandType.getVariables().size(), commandType.name(), values.size()));
        }
    }

    public abstract String execute(Map<CommandVariableType, String> values) throws IllegalArgumentException;

}
