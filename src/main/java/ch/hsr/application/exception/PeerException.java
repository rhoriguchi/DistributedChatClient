package ch.hsr.application.exception;

public class PeerException extends IllegalArgumentException {

    private static final long serialVersionUID = 4032005918465612240L;

    public PeerException(String message) {
        super(message);
    }
}
