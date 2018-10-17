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
    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public int compareTo(IntegerValue o) {
        return Integer.compare(this.toInteger(), o.toInteger());
    }

    public Integer toInteger() {
        return value;
    }


    @Override
    public String toString() {
        return value.toString();
    }
}
