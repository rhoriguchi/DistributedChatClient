package ch.hsr.dsa.domain.common;

public class Username extends StringValue {

    private static final long serialVersionUID = -7102962441285003910L;

    private Username(String username) {
        super(username);
    }

    public static Username fromString(String username) {
        return new Username(username);
    }

    public static Username empty() {
        return new Username("");
    }
}

