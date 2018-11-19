package ch.hsr.domain.keystore;

import ch.hsr.domain.common.StringValue;

// TODO temp name
public class Sign extends StringValue {

    private Sign(String sign) {
        super(sign);
    }

    public static Sign fromString(String sign) {
        return new Sign(sign);
    }

    public static Sign empty() {
        return new Sign("");
    }
}

