package ch.hsr.infrastructure.tomp2p;

import net.tomp2p.dht.FutureGet;
import net.tomp2p.dht.FuturePut;
import net.tomp2p.dht.PeerBuilderDHT;
import net.tomp2p.dht.PeerDHT;
import net.tomp2p.futures.BaseFutureAdapter;
import net.tomp2p.p2p.PeerBuilder;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
import net.tomp2p.tracker.FutureTracker;
import net.tomp2p.tracker.PeerTracker;
import java.io.IOException;


public class TestPeer {

    private static final int PEER_NR_1 = 30;
    private static final int PEER_NR_2 = 77;

    public static void main(String[] args) throws Exception {
        PeerDHT peer1 = new PeerBuilderDHT(new PeerBuilder(Number160.createHash("test1")).ports(4000).start()).start();

        PeerDHT peer2 = new PeerBuilderDHT(new PeerBuilder(Number160.createHash("test2")).ports(4001).start()).start();

        peer1.peer().bootstrap().peerAddress(peer2.peerAddress()).start().awaitListeners();

        peer1.put(Number160.ONE).data(new Data("hallo")).start().awaitListeners();

        Object obj = peer2.get(Number160.ONE).start().awaitUninterruptibly().data().object();

        System.out.println("out: " + obj);
    }

    private static void examplePutGet(final PeerDHT[] peers, final Number160 nr) throws IOException, ClassNotFoundException {
        // default:
        // Replication factor 6, replication not enabled,
        // domain, content, version are zero if not specified

        FuturePut futurePut = peers[PEER_NR_1].put(nr).data(new Data("hallo")).start();
        futurePut.awaitUninterruptibly();
        System.out.println("peer " + PEER_NR_1 + " stored [key: " + nr + ", value \"hallo\"]");
        FutureGet futureGet = peers[PEER_NR_2].get(nr).start();
        System.out.println("peer " + PEER_NR_2 + " got: \"" + futureGet.data().object() + "\" for the key " + nr);
    }

    private static void exampleGetBlocking(final PeerDHT[] peers, final Number160 nr) throws IOException, ClassNotFoundException {
        // Future objects:
        // Keeps track of future events, while the “normal” program flow continues → addListener() or await()
        // await(): Operations are executed in same thread
        // addListener(): Operations are executed in same or other thread

        FutureGet futureGet = peers[PEER_NR_2].get(nr).start();
        // blocking operation
        futureGet.awaitUninterruptibly();
        System.out.println("result blocking: " + futureGet.data().object());
        System.out.println("this may *not* happen before printing the result");
    }

    private static void exampleGetNonBlocking(final PeerDHT[] peers, final Number160 nr) {
        // New utilities necessary (loops as recursions)
        // Advise: use addListener(…) as much as possible!
        // operationComplete(…) must be always called (problem if not) https://github.com/netty/netty/issues/3449

        FutureGet futureGet = peers[PEER_NR_2].get(nr).start();
        // non blocking operations
        futureGet.addListener(new BaseFutureAdapter<FutureGet>() {
            @Override
            public void operationComplete(FutureGet future) throws Exception {
                System.out.println("result non-blocking: " + future.data().object());
            }
        });
        System.out.println("this may happen before printing the result");
    }

    private static void exampleTracker(final PeerTracker[] peers) throws Exception {
        FutureTracker futureTracker = peers[12].addTracker(Number160.createHash("song1"))
            .start()
            .awaitUninterruptibly();
        System.out.println("added myself to the tracker with location [song1]: " + futureTracker.isCompleted() + " I'm: " + peers[12]
            .peerAddress());

        FutureTracker futureTracker2 = peers[24].getTracker(Number160.createHash("song1"))
            .start()
            .awaitUninterruptibly();

        System.out.println("peer 24 got this: " + futureTracker2.trackers());
        System.out.println("currently stored on: " + futureTracker2.trackerPeers());
    }
}