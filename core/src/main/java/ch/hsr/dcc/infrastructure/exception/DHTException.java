package ch.hsr.dcc.infrastructure.exception;

public class DHTException extends IllegalArgumentException {

    private static final long serialVersionUID = -2311029583570540186L;

    public DHTException(String message) {
        super(message);
    }
}
