package ch.hsr.infrastructure.exception;

public class BootstrapException extends IllegalArgumentException {

    private static final long serialVersionUID = 3450457968971582861L;

    public BootstrapException(String message) {
        super(message);
    }
}
