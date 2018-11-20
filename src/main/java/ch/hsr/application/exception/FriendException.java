package ch.hsr.application.exception;

public class FriendException extends IllegalArgumentException {

    private static final long serialVersionUID = -8718025158939451979L;

    public FriendException(String message) {
        super(message);
    }
}
