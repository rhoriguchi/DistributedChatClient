package ch.hsr.application;

import ch.hsr.domain.CommandVariableType;
import java.util.Map;

public interface Command {

    void checkValues(Map<CommandVariableType, String> values) throws IllegalArgumentException;

    String execute(Map<CommandVariableType, String> values) throws IllegalArgumentException;

}
