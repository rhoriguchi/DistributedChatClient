package ch.hsr.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommandObject {

    private CommandType commandType;
    private String[] values;

}
