package ch.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommandObject {

    private CommandType commandType;
    private String[] values;

}
