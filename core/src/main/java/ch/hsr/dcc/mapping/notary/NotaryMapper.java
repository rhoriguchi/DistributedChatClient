package ch.hsr.dcc.mapping.notary;

import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.friend.Friend;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.groupmessage.GroupMessage;
import ch.hsr.dcc.domain.message.Message;
import ch.hsr.dcc.domain.notary.NotaryState;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.infrastructure.blockchain.BlockChainGateway;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PFriendRequest;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupAdd;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;
import org.bouncycastle.util.encoders.Hex;
import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.HashUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Sign;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class NotaryMapper implements NotaryRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotaryMapper.class);

    private final Credentials credentials;

    private final BlockChainGateway blockChainGateway;

    public NotaryMapper(String privateKey,
                        BlockChainGateway blockChainGateway) {
        this.credentials = getCredentials(privateKey);
        this.blockChainGateway = blockChainGateway;
    }

    private Credentials getCredentials(String privateKey) {
        BigInteger privateKeyInteger = getPrivateKey(privateKey);
        BigInteger publicKeyInteger = getPublicKey(privateKeyInteger);

        ECKeyPair ecKeyPair = new ECKeyPair(privateKeyInteger, publicKeyInteger);

        return Credentials.create(ecKeyPair);
    }

    private BigInteger getPublicKey(BigInteger privateKey) {
        return Sign.publicKeyFromPrivate(privateKey);
    }

    private BigInteger getPrivateKey(String privateKey) {
        return ECKey.fromPrivate(Hex.decode(privateKey)).getPrivKey();
    }

    @Override
    public void notarize(TomP2PGroupAdd tomP2PGroupAdd) {
        notarizeGroup(
            tomP2PGroupAdd.getId(),
            tomP2PGroupAdd.getName(),
            tomP2PGroupAdd.getAdminUsername(),
            tomP2PGroupAdd.getLastChanged(),
            tomP2PGroupAdd.getMembers()
        );
    }

    private void notarizeGroup(Long id, String name, String adminUsername, String lastChanged, Collection<String> members) {
        notarizeHash(getGroupHash(id, name, adminUsername, lastChanged, members));
    }

    private byte[] getGroupHash(Long id, String name, String adminUsername, String lastChanged, Collection<String> members) {
        return hash(id, name, adminUsername, lastChanged, members);
    }

    private byte[] hash(Object... objects) {
        String objectsString = Arrays.toString(objects);

        String string = Arrays.stream(objects)
            .map(object -> String.format("%s_%s", objectsString, object.toString()))
            .sorted()
            .collect(Collectors.joining(","));

        return HashUtil.sha3(string.getBytes());
    }

    private void notarizeHash(byte[] hash) {
        blockChainGateway.executeNotarizeHashTransaction(credentials, hash);
    }

    @Override
    public void notarize(TomP2PGroupObject tomP2PGroupObject) {
        notarizeGroup(
            tomP2PGroupObject.getId(),
            tomP2PGroupObject.getName(),
            tomP2PGroupObject.getAdminUsername(),
            tomP2PGroupObject.getLastChanged(),
            tomP2PGroupObject.getMembers()
        );
    }

    @Override
    public void notarize(TomP2PMessage tomP2PMessage) {
        notarizeMessage(
            tomP2PMessage.getFromUsername(),
            tomP2PMessage.getToUsername(),
            tomP2PMessage.getText(),
            tomP2PMessage.getTimeStamp()
        );
    }

    private void notarizeMessage(String fromUsername, String toUsername, String text, String timeStamp) {
        notarizeHash(getMessageHash(fromUsername, toUsername, text, timeStamp));
    }

    private byte[] getMessageHash(String fromUsername, String toUsername, String text, String timeStamp) {
        return hash(fromUsername, toUsername, text, timeStamp);
    }

    @Override
    public void notarize(TomP2PGroupMessage tomP2PGroupMessage) {
        notarizeGroupMessage(
            tomP2PGroupMessage.getToGroupId(),
            tomP2PGroupMessage.getFromUsername(),
            tomP2PGroupMessage.getToUsername(),
            tomP2PGroupMessage.getText(),
            tomP2PGroupMessage.getTimeStamp()
        );
    }

    private void notarizeGroupMessage(Long toGroupId, String fromUsername, String toUsername, String text, String timeStamp) {
        notarizeHash(getGroupMessageHash(toGroupId, fromUsername, toUsername, text, timeStamp));
    }

    private byte[] getGroupMessageHash(Long toGroupId, String fromUsername, String toUsername, String text, String timeStamp) {
        return hash(toGroupId, fromUsername, toUsername, text, timeStamp);
    }

    @Override
    //TODO friend request need some large number so that each request is unique
    public void notarize(TomP2PFriendRequest tomP2PFriendRequest) {
        notarizeFriend(tomP2PFriendRequest.getFromUsername(), tomP2PFriendRequest.getState());
    }

    private void notarizeFriend(String fromUsername, String state) {
        notarizeHash(getFriendHash(fromUsername, state));
    }

    private byte[] getFriendHash(String fromUsername, String state) {
        return hash(fromUsername, state);
    }

    @Override
    public NotaryState verify(Message message) {
        return verify(getMessageHash(
            message.getFromPeer().getUsername().toString(),
            message.getToPeer().getUsername().toString(),
            message.getText().toString(),
            message.getTimeStamp().toString()
        ));
    }

    private NotaryState verify(byte[] hash) {
        try {
            if (blockChainGateway.verifyNotarizedHash(credentials, hash)) {
                return NotaryState.VALID;
            } else {
                return NotaryState.INVALID;
            }
            //TODO to broad exception
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return NotaryState.UNKNOWN;
        }
    }

    @Override
    public NotaryState verify(GroupMessage groupMessage) {
        return verify(getGroupMessageHash(
            groupMessage.getGroup().getId().toLong(),
            groupMessage.getFromPeer().getUsername().toString(),
            //TODO really ugly and needs to be tested
            groupMessage.getToPeers().stream().findFirst().get().getUsername().toString(),
            groupMessage.getText().toString(),
            groupMessage.getTimeStamp().toString()
        ));
    }

    @Override
    public NotaryState verify(Group group) {
        return verify(getGroupHash(
            group.getId().toLong(),
            group.getName().toString(),
            group.getAdmin().getUsername().toString(),
            group.getLastChanged().toString(),
            group.getMembers().stream()
                .map(Peer::getUsername)
                .map(Username::toString)
                .collect(Collectors.toSet())
        ));
    }

    @Override
    public NotaryState verify(Friend friend) {
        return verify(getFriendHash(
            //TODO kinda risky and probably does not work like this
            friend.getFriend().getUsername().toString(),
            friend.getState().name()
        ));
    }
}
