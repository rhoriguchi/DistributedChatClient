package ch.hsr.domain.common;

public class Username extends StringValue {

    private static final long serialVersionUID = -7102962441285003910L;

    private Username(String username) {
        super(username);
    }

    // TODO probably do this in the infrastructure layer
    public static Username fromString(String username) {
        return new Username(username.toLowerCase());
    }

    public static Username empty() {
        return new Username("");
    }
}

