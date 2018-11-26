package ch.hsr.domain.common;

import lombok.EqualsAndHashCode;
import java.io.Serializable;

@EqualsAndHashCode
public class IntegerValue implements Emptyable, Serializable, Comparable<IntegerValue> {

    private static final long serialVersionUID = 4987537479426294701L;

    private final Integer value;

    public IntegerValue(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(IntegerValue o) {
        if (isEmpty() && o.isEmpty()) {
            return 0;
        }
        if (isEmpty()) {
            return -1;
        }
        if (o.isEmpty()) {
            return 1;
        }
        return Integer.compare(this.toInteger(), o.toInteger());
    }

    @Override
    public boolean isEmpty() {
        return value == null;
    }

    public Integer toInteger() {
        return value;
    }

    @Override
    public String toString() {
        if (value != null) {
            return value.toString();
        } else {
            return "";
        }
    }
}
