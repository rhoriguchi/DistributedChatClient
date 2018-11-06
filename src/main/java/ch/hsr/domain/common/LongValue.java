package ch.hsr.domain.common;

import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode
public class LongValue implements Emptyable, Serializable, Comparable<LongValue> {

    private static final long serialVersionUID = -571747015147299019L;

    private final Long value;

    public LongValue(Long value) {
        this.value = value;
    }

    @Override
    public int compareTo(LongValue o) {
        if (isEmpty() && o.isEmpty()) {
            return 0;
        }
        if (isEmpty()) {
            return -1;
        }
        if (o.isEmpty()) {
            return 1;
        }
        return Long.compare(this.toLong(), o.toLong());
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    public Long toLong() {
        return value;
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
