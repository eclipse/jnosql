package org.apache.diana.api.column;


import org.apache.diana.api.Value;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Column Family unit, it is a tuple (pair) that consists of a key-value pair, where the key is mapped to a value.
 * @author Otávio Santana
 */
public class Column implements Serializable {

    private final String name;

    private final Value value;

    private Column(String name, Value value) {
        this.name = Objects.requireNonNull(name, "name is required");
        this.value = Objects.requireNonNull(value, "value is required");
    }

    /**
     * Creates a column instance
     * @param name - column's name
     * @param value - column's value
     * @return a column instance
     * @see Columns
     */
    public static Column of(String name, Value value) {
        return new Column(name, value);
    }

    /**
     * The column's name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * the column's value
     * @return {@link Value}
     */
    public Value getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Column column = (Column) other;
        return Objects.equals(name, column.name) &&
                Objects.equals(value, column.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Column{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
