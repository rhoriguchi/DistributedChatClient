package ch.hsr.domain.user;

import ch.hsr.Constants;
import ch.hsr.domain.common.StringValue;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

public class PeerId extends StringValue {

    private static final long serialVersionUID = -9117328377594923708L;

    private PeerId(String peerId) {
        super(peerId);
    }

    public static PeerId fromString(String peerId) {
        return !isNullOrEmpty(peerId) ? parsePeerId(peerId) : empty();
    }

    private static PeerId parsePeerId(String peerId) {
        checkArgument(isPeerId(peerId), "Invalid Peer Id '%s'", peerId);
        return new PeerId(peerId.replace("0x", ""));
    }

    public static boolean isPeerId(String peerId) {
        return Pattern.compile(Constants.PEER_ID_PATTERN).matcher(peerId).matches();
    }

    public static PeerId empty() {
        return new PeerId("");
    }

    @Override
    public String toString() {
        return "0x" + super.toString();
    }
}

