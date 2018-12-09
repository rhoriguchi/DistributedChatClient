package ch.hsr.dcc.infrastructure.tomp2p.message;

import lombok.Data;
import java.io.Serializable;
import java.util.Collection;

@Data
public class TomP2PGroupAdd implements Serializable {

    private static final long serialVersionUID = 6863974963214624252L;

    private final Long id;
    private final String name;
    private final String adminUsername;
    private final String lastChanged;
    private final Collection<String> members;
    private final String signature;

}
