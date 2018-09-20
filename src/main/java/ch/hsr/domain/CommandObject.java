package ch.hsr.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public class CommandObject {

    private final CommandType commandType;
    private final Map<CommandVariableType, String> values;

    public Map<CommandVariableType, String> getValues() {
        return new HashMap<>(values);
    }
}
