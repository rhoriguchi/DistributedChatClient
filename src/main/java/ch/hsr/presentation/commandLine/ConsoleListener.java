package ch.hsr.presentation.commandLine;

import ch.hsr.application.CommandService;
import ch.hsr.domain.CommandObject;
import ch.hsr.domain.CommandType;
import ch.hsr.domain.CommandVariableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class ConsoleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleListener.class);

    private CommandService commandService;

    public ConsoleListener(CommandService commandService) {
        this.commandService = commandService;
    }

    @PostConstruct
    @Async
    public void InitCommandLineInputListener() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";

        // TODO exit has to be moved to application layer
        while (!line.equalsIgnoreCase(CommandType.EXIT.name())) {
            try {
                line = reader.readLine();
                if (line.trim().length() > 0) {
                    String output = handleLineInput(line);
                    System.out.println(output);
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (NotImplementedException e) {
                // TODO remove later
                System.out.println("Command not implemented");
            } catch (Exception e) {
                LOGGER.debug(e.getMessage(), e);
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            // TODO good solution?
            LOGGER.debug("", e);
        }
    }

    private String handleLineInput(String line) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("^(\\w*)(?:\\s*(.*))$");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches()) {
            CommandType commandType = getCommandTypeCaseInsensitive(matcher.group(1));
            Map<CommandVariableType, String> values = getVariables(commandType, matcher.group(2));

            return commandService.executeCommand(new CommandObject(commandType, values));
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

    private CommandType getCommandTypeCaseInsensitive(String commandType) {
        try {
            return CommandType.valueOf(commandType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input");
        }

    }

    private Map<CommandVariableType, String> getVariables(CommandType commandType, String variableString) {
        if (variableString != null) {
            String regexPattern = commandType.getVariables().stream()
                .map(commandVariable -> String.format("(?<%s>\\w*)", commandVariable.name()))
                .collect(Collectors.joining("\\s*"));

            Pattern pattern = Pattern.compile(String.format("^%s$", regexPattern));
            Matcher matcher = pattern.matcher(variableString);

            if (matcher.matches()) {
                return commandType.getVariables().stream()
                    .collect(Collectors.toMap(
                        commandVariableType -> commandVariableType,
                        commandVariableType -> matcher.group(commandVariableType.name())
                    ));
            } else {
                throw new IllegalArgumentException(String.format("Only %s values allowed for %s but %s used",
                    commandType.getVariables().size(), commandType.name(), variableString.split("\\s").length));
            }
        }

        return new HashMap<>();
    }

}
