package org.eclipse.jnosql.communication.query;


import java.util.Objects;

/**
 * Boolean is a data type that has one of two possible values (usually denoted true and false)
 * which is intended to represent the two truth values of logic and Boolean algebra.
 */
public final class BooleanQueryValue implements QueryValue<Boolean>{

    public static final BooleanQueryValue TRUE = new BooleanQueryValue(true);
    public static final BooleanQueryValue FALSE = new BooleanQueryValue(false);

    private final boolean value;

    private BooleanQueryValue(boolean value) {
        this.value = value;
    }

    @Override
    public ValueType type() {
        return ValueType.BOOLEAN;
    }

    @Override
    public Boolean get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BooleanQueryValue that = (BooleanQueryValue) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "BooleanQueryValue{" +
                "value=" + value +
                '}';
    }
}
