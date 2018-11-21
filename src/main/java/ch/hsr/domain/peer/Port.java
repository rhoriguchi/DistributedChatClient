package ch.hsr.domain.peer;

import ch.hsr.domain.common.LongValue;

// TODO add check that only valid port number can be saved
// TODO use int
public class Port extends LongValue {

    private Port(Long port) {
        super(port);
    }

    public static Port fromLong(Long messageId) {
        return new Port(messageId);
    }

    public static Port empty() {
        return new Port(null);
    }

    public static Port fromInt(int port) {
        return new Port((long) port);
    }
}
