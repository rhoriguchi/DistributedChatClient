package ch.hsr.domain;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public enum CommandType {

    HELP(new ArrayList<>(), ""),
    LOGIN(Lists.newArrayList(CommandVariableType.USERNAME, CommandVariableType.PASSWORD), "Login to chat client"),
    EXIT(new ArrayList<>(), "Exit the application");

    private final List<CommandVariableType> variables;
    private final String description;

    public List<CommandVariableType> getVariables() {
        return new ArrayList<>(variables);
    }

}
