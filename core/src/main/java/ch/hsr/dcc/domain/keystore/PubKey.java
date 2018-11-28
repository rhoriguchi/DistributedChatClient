package ch.hsr.dcc.domain.keystore;

import ch.hsr.dcc.domain.common.StringValue;

public class PubKey extends StringValue {

    private PubKey(String pubKey) {
        super(pubKey);
    }

    public static PubKey fromString(String pubKey) {
        return new PubKey(pubKey);
    }

    public static PubKey empty() {
        return new PubKey("");
    }
}

