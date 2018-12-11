package ch.hsr.dcc.mapping.notary;

import ch.hsr.dcc.domain.friend.Friend;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.groupmessage.GroupMessage;
import ch.hsr.dcc.domain.message.Message;
import ch.hsr.dcc.domain.notary.NotaryState;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupAdd;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;

public interface NotaryRepository {

    void notarize(TomP2PGroupAdd tomP2PGroupAdd);

    void notarize(TomP2PGroupObject tomP2PGroupObject);

    void notarize(TomP2PMessage tomP2PMessage);

    void notarize(TomP2PGroupMessage tomP2PGroupMessage);

    void notarize(TomP2PFriendRequest tomP2PFriendRequest);

    //TODO group add verified?
    //TODO probably do the check on mapping layer

    NotaryState verify(Message message);

    NotaryState verify(GroupMessage groupMessage);

    NotaryState verify(Group group);

    NotaryState verify(Friend friend);
}
