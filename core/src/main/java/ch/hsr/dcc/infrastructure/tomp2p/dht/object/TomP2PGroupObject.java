package ch.hsr.dcc.infrastructure.tomp2p.dht.object;

import lombok.Data;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Data
public class TomP2PGroupObject implements Serializable {

    private static final long serialVersionUID = 2225700331078461183L;

    public final Long id;
    public final String name;
    public final String admin;
    public final Collection<String> members;
    private final String timeStamp;
    public String signature;

    public TomP2PGroupObject(Long id,
                             String name,
                             String admin,
                             Collection<String> members,
                             String timeStamp,
                             String signature) {
        this.id = id;
        this.name = name;
        this.admin = admin;
        this.members = new HashSet<>(members);
        this.timeStamp = timeStamp;
        this.signature = signature;
    }

    public Collection<String> getMembers() {
        return new HashSet<>(members);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TomP2PGroupObject that = (TomP2PGroupObject) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(admin, that.admin) &&
            Objects.equals(members, that.members) &&
            Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, admin, members, timeStamp);
    }
}
