package ch.hsr.dsa.domain.common;

public interface Emptyable {

    default boolean nonEmpty() {
        return !isEmpty();
    }

    boolean isEmpty();

}
