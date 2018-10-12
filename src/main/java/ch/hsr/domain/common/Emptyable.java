package ch.hsr.domain.common;

public interface Emptyable {

    boolean isEmpty();

    default boolean nonEmpty() {
        return !isEmpty();
    }

}
