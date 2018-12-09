package ch.hsr.dcc.mapping.exception;

public class MessageException extends IllegalArgumentException {

    private static final long serialVersionUID = -1881094941222944862L;

    public MessageException(String message) {
        super(message);
    }
}
