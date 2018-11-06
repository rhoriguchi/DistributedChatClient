package ch.hsr.domain.common;

public interface Emptyable {

    default boolean nonEmpty() {
        return !isEmpty();
    }

    boolean isEmpty();

}
