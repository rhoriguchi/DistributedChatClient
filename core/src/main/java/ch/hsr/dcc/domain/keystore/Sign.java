package ch.hsr.dcc.domain.keystore;

import ch.hsr.dcc.domain.common.StringValue;

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

