package ch.hsr.mapping.peer;

import ch.hsr.domain.common.Username;
import ch.hsr.domain.keystore.PubKey;
import ch.hsr.domain.peer.IpAddress;
import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Port;
import ch.hsr.infrastructure.tomp2p.PeerObject;
import ch.hsr.infrastructure.tomp2p.TomP2P;
import ch.hsr.mapping.keystore.KeyStoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.Inet4Address;
import java.net.UnknownHostException;


public class PeerMapper implements PeerRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerMapper.class);

    private final TomP2P tomP2P;
    private final KeyStoreRepository keyStoreRepository;

    public PeerMapper(TomP2P tomP2P, KeyStoreRepository keyStoreRepository) {
        this.tomP2P = tomP2P;
        this.keyStoreRepository = keyStoreRepository;
    }

    @Override
    public void login(IpAddress bootstrapPeerIpAddress, Username username) {
        PubKey pubKey = keyStoreRepository.getPubKeyFromDb(username);
        if (bootstrapPeerIpAddress.isEmpty()) {
            tomP2P.login(username.toString(), pubKey.toString());
        } else {
            tomP2P.login(
                ipAddressToInet4Address(bootstrapPeerIpAddress),
                username.toString(),
                pubKey.toString()
            );
        }
    }

    private Inet4Address ipAddressToInet4Address(IpAddress ipAddress) {
        try {
            return (Inet4Address) Inet4Address.getByName(ipAddress.toString());
        } catch (UnknownHostException e) {
            LOGGER.error(e.getMessage(), e);

            throw new IllegalArgumentException("IPv4 Address is not valid");
        }
    }

    @Override
    public void logout() {
        tomP2P.logout();
    }

    @Override
    public Peer getSelf() {
        return peerObjectToPeer(tomP2P.getSelf());
    }

    private Peer peerObjectToPeer(PeerObject peerObject) {
        return new Peer(
            Username.fromString(peerObject.getUsername()),
            IpAddress.fromString(peerObject.getIpAddress()),
            Port.fromInt(peerObject.getTcpPort()),
            Port.fromInt(peerObject.getUdpPort()),
            true
        );
    }

    @Override
    public Peer getPeer(Username username) {
        return tomP2P.getPeerObject(username.toString())
            .map(this::peerObjectToPeer)
            .orElse(new Peer(
                username,
                IpAddress.empty(),
                Port.empty(),
                Port.empty(),
                false
            ));
    }
}
