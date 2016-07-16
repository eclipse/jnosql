package org.apache.diana.api.column;


import org.apache.diana.api.Value;

import java.io.Serializable;
import java.util.Objects;

public class Column implements Serializable {

    private final String name;

    private final Value value;

    public Column(String name, Value value) {
        this.name = Objects.requireNonNull(name, "name is required");
        this.value = Objects.requireNonNull(value, "value is required");
    }

    public String getName() {
        return name;
    }

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
        final StringBuilder sb = new StringBuilder("Document{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
