package ch.hsr.dcc.application.exception;

public class GroupException extends IllegalArgumentException {

    private static final long serialVersionUID = 6572172445965359295L;

    public GroupException(String message) {
        super(message);
    }
}
