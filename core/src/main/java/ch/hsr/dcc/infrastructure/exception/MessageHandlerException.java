package ch.hsr.dcc.infrastructure.exception;

public class MessageHandlerException extends IllegalArgumentException {

    private static final long serialVersionUID = -8303522605762010374L;

    public MessageHandlerException(String message) {
        super(message);
    }
}
