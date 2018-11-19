package ch.hsr.application.exception;

public class MessageException extends IllegalArgumentException {

    private static final long serialVersionUID = 8984984583286022684L;

    public MessageException(String message) {
        super(message);
    }
}
