package org.apache.diana.api.util;


import org.apache.diana.api.Value;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class DefaultValue implements Value {


    private final Serializable value;

    private DefaultValue(Serializable value) {
        this.value = value;
    }

    public static Value of(Serializable value) {
        Objects.requireNonNull(value);
        return new DefaultValue(value);
    }

    @Override
    public Serializable get() {
        return value;
    }

    @Override
    public <T extends Serializable> T get(Class<T> clazz) {
        return clazz.cast(value);
    }

    @Override
    public <T extends Serializable> List<T> getList(Class<T> clazz) {
        return (List<T>) value;
    }

    @Override
    public <T extends Serializable> Stream<T> getStream(Class<T> clazz) {
        return getList(clazz).stream();
    }

    @Override
    public <T extends Serializable> Set<T> getSet(Class<T> clazz) {
        return (Set<T>) value;
    }

    @Override
    public <K extends Serializable, V extends Serializable> Map<K, V> getSet(Class<K> keyClass, Class<V> valueClass) {
        return (Map<K, V>) value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultValue that = (DefaultValue) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultValue{");
        sb.append("value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}
