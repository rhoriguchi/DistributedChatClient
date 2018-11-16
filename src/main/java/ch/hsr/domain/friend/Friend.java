package ch.hsr.domain.friend;

import ch.hsr.domain.common.Peer;
import lombok.Data;

@Data
public class Friend {

    private final Peer friend;
    private final Peer self;
    // TODO add logic to have state (enum), acknowledged, sent,...

}
