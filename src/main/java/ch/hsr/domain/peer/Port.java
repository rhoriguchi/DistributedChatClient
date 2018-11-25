package ch.hsr.domain.peer;

import ch.hsr.domain.common.IntegerValue;

//TODO add check that only valid port number can be saved
public class Port extends IntegerValue {

    private Port(Integer port) {
        super(port);
    }

    public static Port empty() {
        return new Port(null);
    }

    public static Port fromInteger(Integer port) {
        return new Port(port);
    }
}
