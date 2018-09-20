package ch.hsr.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CommandObject {

    private CommandType commandType;
    private Map<CommandVariableType, String> values;

}
