package ch.hsr.domain.keystore;

import ch.hsr.domain.common.StringValue;

// TODO temp name
public class Sign extends StringValue {

    private Sign(String signature) {
        super(signature);
    }

    public static Sign fromString(String signature) {
        return new Sign(signature);
    }

    public static Sign empty() {
        return new Sign("");
    }
}

