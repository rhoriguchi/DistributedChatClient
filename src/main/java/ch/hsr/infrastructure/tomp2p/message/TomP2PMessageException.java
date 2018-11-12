package ch.hsr.infrastructure.tomp2p.message;

// TODO not used
public class TomP2PMessageException extends IllegalArgumentException {

    private static final long serialVersionUID = -9188649602517335847L;

    private final TomP2PMessage tomP2PMessage;

    public TomP2PMessageException(String message, TomP2PMessage tomP2PMessage) {
        super(message);
        this.tomP2PMessage = tomP2PMessage;
    }
}
