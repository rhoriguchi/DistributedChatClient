package ch.hsr;

public class Constants {

    // TODO use better pattern
    public static final String IP_ADDRESS_PATTERN = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
    public static final String PEER_ID_PATTERN = "^(0x)?[0-9a-f]{40}$";

    private Constants() {
        throw new IllegalAccessError("This class is only for constants");
    }
}