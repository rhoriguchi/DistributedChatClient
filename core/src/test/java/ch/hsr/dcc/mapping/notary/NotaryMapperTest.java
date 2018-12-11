package ch.hsr.dcc.mapping.notary;

import ch.hsr.dcc.DistributedChatClientConfiguration;
import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.friend.Friend;
import ch.hsr.dcc.domain.friend.FriendState;
import ch.hsr.dcc.domain.notary.NotaryState;
import ch.hsr.dcc.domain.peer.IpAddress;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.domain.peer.Port;
import ch.hsr.dcc.infrastructure.db.DbIdGenerator;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

// This test is only for demo purpose since this should not be tested like this

@RunWith (SpringJUnit4ClassRunner.class)
@SpringBootTest (classes = DistributedChatClientConfiguration.class)
public class NotaryMapperTest {

    @Autowired
    private NotaryRepository notaryRepository;

    @Test
    // is ignored since this can trigger a timeout in travis
    @Ignore
    //TODO can cause error if id is already in use
    //TODO class changed only for test which is bad solution
    public void testGroupNotary() {
        TomP2PFriendRequest tomP2PFriendRequest = new TomP2PFriendRequest(
            DbIdGenerator.getId().toString(),
            FriendState.TEST.name(),
            false
        );

        Friend friend = new Friend(
            new Peer(
                Username.fromString(tomP2PFriendRequest.getFromUsername()),
                IpAddress.empty(),
                Port.empty(),
                Port.empty(),
                true
            ),
            new Peer(
                Username.fromString("Self"),
                IpAddress.empty(),
                Port.empty(),
                Port.empty(),
                true
            ),
            tomP2PFriendRequest.isFailed(),
            FriendState.valueOf(tomP2PFriendRequest.getState())
        );

        notaryRepository.notarize(tomP2PFriendRequest);
        assertThat(notaryRepository.verify(friend), equalTo(NotaryState.VALID));
    }
}
