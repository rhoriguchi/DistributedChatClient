package ch.hsr.dcc.infrastructure.tomp2p.dht.object;

import lombok.Data;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

@Data
public class TomP2PGroupObject implements Serializable {

    private static final long serialVersionUID = 1656146635623189962L;

    public final Long id;
    public final String name;
    public final String adminUsername;
    public final Collection<String> members;
    private final String lastChanged;
    public String signature;

    public TomP2PGroupObject(Long id,
                             String name,
                             String adminUsername,
                             Collection<String> members,
                             String lastChanged,
                             String signature) {
        this.id = id;
        this.name = name;
        this.adminUsername = adminUsername;
        this.members = new HashSet<>(members);
        this.lastChanged = lastChanged;
        this.signature = signature;
    }

    public Collection<String> getMembers() {
        return new HashSet<>(members);
    }
}
