package org.apache.diana.api.document;


import org.apache.diana.api.Value;

import java.util.Objects;

/**
 * A default implementation {@link Document}
 */
final class DefaultDocument implements Document {

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultDocument)) {
            return false;
        }
        Document that = (Document) o;
        return Objects.equals(name, that.getName()) &&
                Objects.equals(value, that.getValue());
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
