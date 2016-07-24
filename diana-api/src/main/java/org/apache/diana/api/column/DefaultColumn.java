package org.apache.diana.api.column;

import org.apache.diana.api.Value;

import java.util.Objects;


final class DefaultColumn implements Column {

    private final String name;

    private final Value value;

    DefaultColumn(String name, Value value) {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Column)){
            return false;
        }
        Column that = (Column) o;
        return Objects.equals(name, that.getName()) &&
                Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultColumn{");
        sb.append("name='").append(name).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
