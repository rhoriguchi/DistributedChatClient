package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.peers.Number160;
import net.tomp2p.peers.PeerAddress;

public interface DistributedHashTable {

    boolean login(PeerAddress bootstrapPeerAddress, String username);

    void logout();

    PeerObject getSelf();

    PeerObject getPeer(PeerAddress peerAddress);

    boolean addStringToDHT(Number160 key, String value);
}
