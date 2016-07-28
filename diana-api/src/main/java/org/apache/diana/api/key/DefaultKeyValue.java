package org.apache.diana.api.key;

import org.apache.diana.api.Value;

import java.util.Objects;

/**
 * The default implementation of {@link KeyValue}
 */
final class DefaultKeyValue<T> implements KeyValue {

    private final T key;

    private final Value value;

    DefaultKeyValue(T key, Value value) {
        this.key = Objects.requireNonNull(key, "key is required");
        this.value = Objects.requireNonNull(value, "value is required");
    }


    public T getKey() {
        return key;
    }

    public Value getValue() {
        return value;
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
