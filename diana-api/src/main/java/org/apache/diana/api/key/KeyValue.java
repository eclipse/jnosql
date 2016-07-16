package org.apache.diana.api.key;


import org.apache.diana.api.Value;

import java.util.Objects;

public class KeyValue {

    private final String key;

    private final Value value;

    public KeyValue(String key, Value value) {
        this.key = Objects.requireNonNull(key, "key is required");
        this.value = Objects.requireNonNull(value, "value is required");
    }

    public String getKey() {
        return key;
    }

    public Value getValue() {
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
        KeyValue keyValue = (KeyValue) o;
        return Objects.equals(key, keyValue.key) &&
                Objects.equals(value, keyValue.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KeyValue{");
        sb.append("key='").append(key).append('\'');
        sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
