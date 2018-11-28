package ch.hsr.dcc.domain.common;

public interface Emptyable {

    default boolean nonEmpty() {
        return !isEmpty();
    }

    boolean isEmpty();

}
