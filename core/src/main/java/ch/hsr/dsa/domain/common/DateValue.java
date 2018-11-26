package ch.hsr.dsa.domain.common;

import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class DateValue implements Emptyable, Serializable, Comparable<DateValue> {

    private static final long serialVersionUID = -6069899982724412783L;

    private final Instant value;

    public DateValue(Instant value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        } else {
            return value.toString();
        }
    }

    @Override
    public boolean isEmpty() {
        return value == null || value == Instant.MIN;
    }

    public String toString(DateTimeFormatter formatter) {
        if (isEmpty()) {
            return "";
        } else {
            return formatter.format(value);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DateValue dateValue = (DateValue) o;
        return Objects.equals(value, dateValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(DateValue o) {
        if (this.isEmpty()) {
            if (o.isEmpty()) {
                return 0;
            } else {
                return -1;
            }
        } else if (o.isEmpty()) {
            return 1;
        }
        return this.value.compareTo(o.getValue());
    }

    private Instant getValue() {
        return value;
    }

    public boolean isBeforeOrEqual(DateValue other) {
        return 0 >= getValue().compareTo(other.getValue());
    }

    public boolean isAfterOrEqual(DateValue other) {
        return 0 <= getValue().compareTo(other.getValue());
    }

}
