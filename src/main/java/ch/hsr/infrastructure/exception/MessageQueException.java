package ch.hsr.infrastructure.exception;

public class MessageQueException extends IllegalArgumentException {

    private static final long serialVersionUID = -6506687171033699740L;

    public MessageQueException(String message) {
        super(message);
    }
}
