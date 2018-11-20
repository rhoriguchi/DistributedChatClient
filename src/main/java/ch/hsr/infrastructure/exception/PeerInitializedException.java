package ch.hsr.infrastructure.exception;

public class PeerInitializedException extends IllegalArgumentException {

    private static final long serialVersionUID = -2297581263788088453L;

    public PeerInitializedException(String message) {
        super(message);
    }
}
