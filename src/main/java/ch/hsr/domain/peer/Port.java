package ch.hsr.domain.peer;

import ch.hsr.domain.common.IntegerValue;

import static com.google.common.base.Preconditions.checkArgument;

public class Port extends IntegerValue {

    private Port(Integer port) {
        super(port);
    }

    public static Port empty() {
        return new Port(null);
    }

    public static Port fromInteger(Integer port) {
        checkArgument(isValidPort(port), "Invalid Port '%s'", port);
        return new Port(port);
    }

    public static boolean isValidPort(Integer port) {
        if (port == null) {
            return true;
        } else {
            return port >= 1 && port <= 65535;
        }
    }
}
