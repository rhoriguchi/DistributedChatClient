package ch.hsr.domain.user;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Base64;

@Data
public class PeerAddress implements Serializable {

    private static final long serialVersionUID = 3366838525449964479L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PeerAddress.class);

    private final PeerId peerId;
    private final InetAddress inetAddress;
    private final Port tcpPort;
    private final Port udpPort;

    public static PeerAddress fromSerialize(String value) {
        byte[] data = Base64.getDecoder().decode(value.getBytes());

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            Object object = objectInputStream.readObject();

            return (PeerAddress) object;
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new IllegalArgumentException("Peer Address could not be parsed");
        }
    }

    public String serialize() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(this);
                objectOutputStream.flush();

                return new String(Base64.getEncoder().encode(byteArrayOutputStream.toByteArray()));
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return null;
    }
}

