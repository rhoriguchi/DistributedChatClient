package ch.hsr.domain.common;

public class Username extends StringValue {

    private static final long serialVersionUID = 2970568018114842669L;

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

