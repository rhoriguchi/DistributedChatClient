package ch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum CommandType {

    HELP("/help", ""),
    JOIN("/join", "Join network"),
    LEAVE("/leave", "Leave network"),
    EXIT("/exit", "Exit the application");

    private String command;
    private String description;

    public static CommandType getCommandType(String command) {
        return Arrays.stream(CommandType.class.getEnumConstants())
            .filter(commandType -> commandType.getCommand().equals(command))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Command does not exist"));
    }

}
