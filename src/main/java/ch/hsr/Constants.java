package ch.hsr;

public class Constants {

    // TODO maybe use enum for matcher

    public static final String PEER_ID_PATTERN = "^(?<id>(?:0x)?(?:[0-9a-f]{40}))$";
    // TODO might still be wrong
    public static final String PEER_ADDRESS_PATTERN = "^paddr\\[(?<id>(?:0x)(?:[0-9a-f]{40}))\\[\\/(?<socketAddress>(?<ip>(?:\\d{1,3}\\.){3}\\d{1,3}),(?:(?<port>\\d{1,5})|t:(?<udpPort>\\d{1,5}),u:(?<tcpPort>\\d{1,5})))\\]\\]\\/relay\\((?:(?<relayFlagFalse>false)\\)|(?<relayFlagTrue>true),(?<relaySocketAddressSize>\\d+)\\)=(?<relaySocketAddress>(?:\\[\\/(?:\\d{1,3}\\.){3}\\d{1,3},(?:\\d{1,5}|t:\\d{1,5},u:\\d{1,5})\\])+))\\/slow\\((?<slowFlag>true|false)\\)$";

    private Constants() {
        throw new IllegalAccessError("This class is only for constants");
    }
}