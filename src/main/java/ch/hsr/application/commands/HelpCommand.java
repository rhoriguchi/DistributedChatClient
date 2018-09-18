package ch.hsr.application.commands;


import ch.hsr.application.Command;
import ch.hsr.domain.CommandType;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HelpCommand implements Command {

    private final String helpText;

    public HelpCommand() {
        this.helpText = Arrays.stream(CommandType.class.getEnumConstants())
            .filter(commandType -> !commandType.equals(CommandType.HELP))
            .map(commandType -> String.format("%s - %s", commandType.getCommand(), commandType.getDescription()))
            .collect(Collectors.joining("\n"));
    }

    @Override
    public void checkValues(String... values) throws IllegalArgumentException {
        if (values != null && values.length > 0) {
            throw new IllegalArgumentException(String.format("No values allowed with %s",
                CommandType.HELP.getCommand()));
        }
    }

    @Override
    public String execute(String... values) {
        return helpText;
    }
}
