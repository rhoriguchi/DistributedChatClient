package ch.hsr.dsa.mapping.exception;

public class NotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = -7170009522968737750L;

    public NotFoundException(String message) {
        super(message);
    }
}
