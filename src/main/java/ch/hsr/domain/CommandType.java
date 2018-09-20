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

    private String command;
    private List<CommandVariableType> variables;
    private String description;

    public static CommandType getCommandType(String command) {
        return Arrays.stream(CommandType.class.getEnumConstants())
            .filter(commandType -> commandType.getCommand().equals(command))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Command does not exist"));
    }

}
