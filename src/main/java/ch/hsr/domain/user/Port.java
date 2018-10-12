package ch.hsr.domain.user;

import ch.hsr.domain.common.IntegerValue;

public class Port extends IntegerValue {

    private static final long serialVersionUID = -4172599267873434760L;

    public Port(Integer port) {
        super(port);
    }

    public static Port fromInteger(Integer port) {
        return new Port(port);
    }

    public static Port empty() {
        return new Port(null);
    }
}
