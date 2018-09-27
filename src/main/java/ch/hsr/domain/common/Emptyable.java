package ch.hsr.domain.common;

import java.util.Collection;
import java.util.Optional;

public interface Emptyable {

    static <T extends Emptyable> T defaultIfEmpty(T value, T defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        return !value.isEmpty() ?
            value :
            defaultValue;
    }

    boolean isEmpty();

    static <T extends Collection> T defaultIfEmpty(T collection, T defaultValue) {
        if (collection == null) {
            return defaultValue;
        }
        return !collection.isEmpty() ?
            collection :
            defaultValue;
    }

    static <T extends Emptyable> Optional<T> toOptional(T value) {
        if (value == null) {
            return Optional.empty();
        }
        return !value.isEmpty() ?
            Optional.of(value) :
            Optional.empty();
    }

    default boolean nonEmpty() {
        return !isEmpty();
    }

}
