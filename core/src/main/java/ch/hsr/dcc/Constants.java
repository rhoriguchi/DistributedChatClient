package ch.hsr.dcc;

public class Constants {

    public static final String IP_ADDRESS_PATTERN = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";

    public static final String SMART_CONTRACT_ADDRESS = "0xf37d0aa19ec6a0340ccf09c4ba2cfb34069298b3";
    //TODO rename in smart contract
    public static final String SMART_CONTRACT_NOTARIZE = "notarizeMessage";
    public static final String SMART_CONTRACT_VERIFY = "verifyMessage";

    private Constants() {
        throw new IllegalAccessError("This class is only for constants");
    }
}