package ch.hsr.dcc.infrastructure.exception;

public class PeerHolderException extends IllegalArgumentException {

    private static final long serialVersionUID = 3976722250910812949L;

    public PeerHolderException(String message) {
        super(message);
    }
}
