package ch.presentation.commandLine;

import ch.application.CommandService;
import ch.domain.CommandObject;
import ch.domain.CommandType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class ConsoleListener {

    private CommandService commandService;

    public ConsoleListener(CommandService commandService) {
        this.commandService = commandService;
        InitCommandLineInputListener();
    }

    @Async
    public void InitCommandLineInputListener() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = "";

        while (!line.equalsIgnoreCase(CommandType.EXIT.getCommand())) {
            try {
                String output = handleLineInput(reader.readLine());
                System.out.println(output);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                // TODO log
            }
        }

        try {
            reader.close();
        } catch (IOException e) {
            // TODO do something with this
        }
    }

    private String handleLineInput(String line) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("^(/\\w*)(?:(\\s.*)*)$");
        Matcher matcher = pattern.matcher(line);

        if (matcher.matches() && matcher.group(1) != null) {
            CommandType commandType = CommandType.getCommandType(matcher.group(1));

            String[] values = null;
            if (matcher.group(2) != null) {
                values = matcher.group(2).split("\\s*");
            }

            return commandService.executeCommand(new CommandObject(commandType, values));
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
    }

}
