package ch.hsr.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CommandType {

    HELP("/help", "", ""),
    LOGIN("/login", "[USERNAME] [PASSWORD]", "Login to chat client"),
    EXIT("/exit", "", "Exit the application");

    private String command;
    // TODO regex pattern or something so this can be accessed with matcher
    private String variables;
    private String description;

    public static CommandType getCommandType(String command) {
        return Arrays.stream(CommandType.class.getEnumConstants())
            .filter(commandType -> commandType.getCommand().equals(command))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Command does not exist"));
    }

}
