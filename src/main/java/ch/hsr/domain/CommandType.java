package ch.hsr.domain;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum CommandType {

    HELP("/help", new ArrayList<>(), ""),
    LOGIN("/login",
        Lists.newArrayList(CommandVariableType.USERNAME, CommandVariableType.PASSWORD),
        "Login to chat client"),
    EXIT("/exit", new ArrayList<>(), "Exit the application");

    private final String command;
    private final List<CommandVariableType> variables;
    private final String description;

    public List<CommandVariableType> getVariables() {
        return new ArrayList<>(variables);
    }

    public static CommandType getCommandType(String command) {
        return Arrays.stream(CommandType.class.getEnumConstants())
            .filter(commandType -> commandType.getCommand().equals(command))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Command does not exist"));
    }

}
