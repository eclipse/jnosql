package org.apache.diana.api.document;


import org.apache.diana.api.Value;

import java.util.Objects;

final class DefaultDocument implements Document{
    private final String name;

    private final Value value;


    DefaultDocument(String name, Value value) {
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
        DefaultDocument document = (DefaultDocument) other;
        return Objects.equals(name, document.name) &&
                Objects.equals(value, document.value);
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
