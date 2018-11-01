package ch.hsr.domain.peer;

import lombok.Data;

@Data
public class Peer {

    private final Username username;
    private final IpAddress ipAddress;
}
