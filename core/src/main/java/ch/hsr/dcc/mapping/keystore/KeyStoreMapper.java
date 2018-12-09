package ch.hsr.dcc.mapping.keystore;

import ch.hsr.dcc.domain.common.Username;
import ch.hsr.dcc.domain.group.Group;
import ch.hsr.dcc.domain.keystore.PubKey;
import ch.hsr.dcc.domain.keystore.Sign;
import ch.hsr.dcc.domain.keystore.SignState;
import ch.hsr.dcc.domain.peer.Peer;
import ch.hsr.dcc.infrastructure.db.DbFriend;
import ch.hsr.dcc.infrastructure.db.DbGateway;
import ch.hsr.dcc.infrastructure.db.DbKeyPair;
import ch.hsr.dcc.infrastructure.tomp2p.TomP2P;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PGroupObject;
import ch.hsr.dcc.infrastructure.tomp2p.dht.object.TomP2PPeerObject;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PGroupMessage;
import ch.hsr.dcc.infrastructure.tomp2p.message.TomP2PMessage;
import ch.hsr.dcc.mapping.exception.SignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class KeyStoreMapper implements KeyStoreRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyStoreMapper.class);

    private final DbGateway dbGateway;
    private final TomP2P tomP2P;

    private final KeyPairGenerator keyPairGenerator;
    private final KeyFactory keyFactory;

    public KeyStoreMapper(DbGateway dbGateway,
                          TomP2P tomP2P,
                          KeyPairGenerator keyPairGenerator,
                          KeyFactory keyFactory) {
        this.dbGateway = dbGateway;
        this.tomP2P = tomP2P;
        this.keyPairGenerator = keyPairGenerator;
        this.keyFactory = keyFactory;
    }

    @Override
    public PubKey getPubKeyFromDb(Username username) {
        PublicKey publicKey = getKeyPair(username).getPublic();
        return PubKey.fromString(encodeKey(publicKey));
    }

    @Override
    public Sign sign(Group group) {
        return signGroup(
            group.getId().toLong(),
            group.getName().toString(),
            group.getAdmin().getUsername().toString(),
            group.getLastChanged().toString(),
            group.getMembers().stream()
                .map(Peer::getUsername)
                .map(Username::toString)
                .collect(Collectors.toSet())
        );
    }

    private Sign signGroup(Long id, String name, String adminUsername, String lastChanged, Collection<String> members) {
        return sign(Objects.hash(id, name, adminUsername, lastChanged, members));
    }

    private Sign sign(int hashCode) {
        KeyPair keyPair = getKeyPair(getOwnUsername());

        try {
            Signature signature = getSignature();
            signature.initSign(keyPair.getPrivate());
            signature.update(Integer.valueOf(hashCode).byteValue());

            return Sign.fromString(encodeBase64(signature.sign()));
        } catch (InvalidKeyException | SignatureException e) {
            LOGGER.error(e.getMessage(), e);
            throw new SignException("Hash code can't be signed");
        }
    }

    private KeyPair getKeyPair(Username username) {
        return dbGateway.getKeyPair(username.toString())
            .map(this::dbKeyPairToKeyPair)
            .orElseGet(() -> generateAndSaveNewKeyPair(username));
    }

    private KeyPair generateAndSaveNewKeyPair(Username username) {
        LOGGER.info("Generating KeyPair...");
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        LOGGER.info("Generating KeyPair completed");

        dbGateway.saveKeyPair(
            new DbKeyPair(
                username.toString(),
                encodeKey(keyPair.getPrivate()),
                encodeKey(keyPair.getPublic())
            )
        );

        return keyPair;
    }

    private String encodeKey(Key key) {
        return encodeBase64(key.getEncoded());
    }

    private String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private Username getOwnUsername() {
        return Username.fromString(tomP2P.getSelf().getUsername());
    }

    private Signature getSignature() {
        try {
            return Signature.getInstance("SHA1WithRSA");
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("Signature can't be initialized");
        }
    }

    @Override
    public Sign sign(TomP2PGroupObject tomP2PGroupObject) {
        return signGroup(
            tomP2PGroupObject.getId(),
            tomP2PGroupObject.getName(),
            tomP2PGroupObject.getAdminUsername(),
            tomP2PGroupObject.getLastChanged(),
            tomP2PGroupObject.getMembers()
        );
    }

    @Override
    public Sign sign(TomP2PMessage tomP2PMessage) {
        return signMessage(
            tomP2PMessage.getFromUsername(),
            tomP2PMessage.getToUsername(),
            tomP2PMessage.getText(),
            tomP2PMessage.getTimeStamp()
        );
    }

    private Sign signMessage(String fromUsername, String toUsername, String text, String timeStamp) {
        return sign(Objects.hash(fromUsername, toUsername, text, timeStamp));
    }

    @Override
    public Sign sign(TomP2PGroupMessage tomP2PGroupMessage) {
        return signGroupMessage(
            tomP2PGroupMessage.getToGroupId(),
            tomP2PGroupMessage.getFromUsername(),
            tomP2PGroupMessage.getToUsername(),
            tomP2PGroupMessage.getText(),
            tomP2PGroupMessage.getTimeStamp()
        );
    }

    private Sign signGroupMessage(Long toGroupId, String fromUsername, String toUsername, String text, String timeStamp) {
        return sign(Objects.hash(toGroupId, fromUsername, toUsername, text, timeStamp));
    }

    @Override
    public Sign sign(DbFriend dbFriend) {
        return singFriend(dbFriend.getUsername(), dbFriend.getOwnerUsername(), dbFriend.getState());
    }

    private Sign singFriend(String username, String ownerUsername, String state) {
        return sign(Objects.hash(username, ownerUsername, state));
    }

    private KeyPair dbKeyPairToKeyPair(DbKeyPair dbKeyPair) {
        return new KeyPair(
            decodePublicKey(dbKeyPair.getPublicKey()),
            decodePrivateKey(dbKeyPair.getPrivateKey())
        );
    }

    private PublicKey decodePublicKey(String key) {
        try {
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decodeBase64(key));
            return keyFactory.generatePublic(x509EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.error(e.getMessage(), e);
            throw new SignException("Public key can't be decoded");
        }
    }

    private byte[] decodeBase64(String string) {
        return Base64.getDecoder().decode(string);
    }

    private PrivateKey decodePrivateKey(String key) {
        try {
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decodeBase64(key));
            return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } catch (InvalidKeySpecException e) {
            LOGGER.error(e.getMessage(), e);
            throw new SignException("Private key can't be decoded");
        }
    }

    @Override
    public SignState checkSignature(Username username, Sign sign, int hashCode) {
        return tomP2P.getPeerObject(username.toString())
            .map(TomP2PPeerObject::getPublicKey)
            .map(this::decodePublicKey)
            .map(publicKey -> {
                try {
                    Signature signature = getSignature();
                    signature.verify(publicKey.getEncoded());
                    signature.update(Integer.valueOf(hashCode).byteValue());

                    if (signature.verify(decodeBase64(sign.toString()))) {
                        return SignState.VALID;
                    } else {
                        return SignState.INVALID;
                    }
                } catch (SignatureException e) {
                    LOGGER.error(e.getMessage(), e);
                    return SignState.UNKNOWN;
                }
            }).orElseGet(() -> SignState.UNKNOWN);
    }
}
