package ch.hsr.domain.common;

import java.io.Serializable;

import static com.google.common.base.Strings.nullToEmpty;

public class StringValue implements Emptyable, Serializable, Comparable<StringValue> {

    private static final long serialVersionUID = 2269711799039289527L;

    private final String value;

    public StringValue(String value) {
        this.value = nullToEmpty(value);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object that) {
        return this == that || that != null &&
            getClass() == that.getClass() &&
            value.equals(((StringValue) that).value);
    }

    @Override
    public int compareTo(StringValue o) {
        return this.toString().compareToIgnoreCase(o.toString());
    }

    @Override
    public String toString() {
        return value;
    }
}
