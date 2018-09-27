package ch.hsr.domain.user;

import ch.hsr.domain.common.StringValue;

public class Username extends StringValue {

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

