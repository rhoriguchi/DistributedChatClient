package ch.hsr.application.commands;


import ch.hsr.application.Command;
import ch.hsr.domain.CommandType;
import ch.hsr.domain.CommandVariableType;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HelpCommand extends Command {

    private final String helpText;

    public HelpCommand(CommandType commandType) {
        super(commandType);
        helpText = Arrays.stream(CommandType.class.getEnumConstants())
            .filter(type -> !type.equals(CommandType.HELP))
            .map(type -> {
                String command;
                if (type.getVariables().size() > 0) {
                    String commandVariables = type.getVariables().stream()
                        .map(commandVariable -> String.format("[%s]", commandVariable.name()))
                        .collect(Collectors.joining(" "));

                    command = String.format("%s %s", type.name().toLowerCase(), commandVariables);
                } else {
                    command = type.name().toLowerCase();
                }

                return String.format("%s - %s", command, type.getDescription());
            }).collect(Collectors.joining("\n"));
    }

    @Override
    public String execute(Map<CommandVariableType, String> values) {
        return helpText;
    }
}
