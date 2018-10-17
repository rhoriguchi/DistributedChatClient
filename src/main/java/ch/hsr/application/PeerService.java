package ch.hsr.application;

import ch.hsr.domain.peer.Peer;
import ch.hsr.domain.peer.Username;
import ch.hsr.domain.peer.peeraddress.PeerAddress;
import ch.hsr.mapping.peer.PeerRepository;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PeerService {

    private final PeerRepository peerRepository;

    public PeerService(PeerRepository peerRepository) {
        this.peerRepository = peerRepository;
    }

    // TODO return success or something, probably use timer as well after certain time return false
    // https://stackoverflow.com/a/9951741
    public boolean login(PeerAddress bootstrapPeerAddress, Username username) {
        // TODO commented
//        peerRepository.login(bootstrapPeerAddress, username);
        return true;
    }

    // TODO get real peer object collection
    public Set<Peer> getPeer() {
        return IntStream.rangeClosed(0, 100)
            .mapToObj(String::valueOf)
            .map(username -> new Peer(
                Username.fromString(username),
                PeerAddress.empty()
            )).collect(Collectors.toSet());
    }

    // TODO need to get this from a deper layer after the login happens
    public Peer getSelf() {
        return new Peer(
            Username.fromString("Mock"),
            PeerAddress.empty()
        );
    }
}
