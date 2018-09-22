package ch.hsr.application.commands;


import ch.hsr.application.Command;
import ch.hsr.domain.CommandType;
import ch.hsr.domain.CommandVariableType;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HelpCommand implements Command {

    private final String helpText;

    public HelpCommand() {
        this.helpText = Arrays.stream(CommandType.class.getEnumConstants())
            .filter(commandType -> !commandType.equals(CommandType.HELP))
            .map(commandType -> {
                String command;
                if (commandType.getVariables() != null && commandType.getVariables().size() > 0) {
                    String commandVariables = commandType.getVariables().stream()
                        .map(commandVariable -> String.format("[%s]", commandVariable.name()))
                        .collect(Collectors.joining(" "));

                    command = String.format("%s %s", commandType.name().toLowerCase(), commandVariables);
                } else {
                    command = commandType.name().toLowerCase();
                }

                return String.format("%s - %s", command, commandType.getDescription());
            })
            .collect(Collectors.joining("\n"));
    }

    @Override
    public void checkValues(Map<CommandVariableType, String> values) throws IllegalArgumentException {
        if (values.size() > 0) {
            throw new IllegalArgumentException(String.format("No values allowed with %s",
                CommandType.HELP));
        }
    }

    @Override
    public String execute(Map<CommandVariableType, String> values) {
        return helpText;
    }
}
