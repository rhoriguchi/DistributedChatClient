package ch.hsr.domain.common;

import lombok.EqualsAndHashCode;
import java.io.Serializable;

import static com.google.common.base.Strings.nullToEmpty;

@EqualsAndHashCode
public class StringValue implements Emptyable, Serializable, Comparable<StringValue> {
    
    private static final long serialVersionUID = 6157604860567302981L;

    private final String value;

    public StringValue(String value) {
        this.value = nullToEmpty(value);
    }

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
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
