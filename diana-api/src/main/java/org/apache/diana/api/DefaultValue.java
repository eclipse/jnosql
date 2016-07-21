package org.apache.diana.api;


import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

class DefaultValue implements Value {

    private static transient final ReaderFieldProvider SERVICE_PROVIDER = ReaderFieldProvider.getInstance();

    private final Object value;

    private DefaultValue(Object value) {
        this.value = value;
    }

    public static Value of(Object value) {
        Objects.requireNonNull(value);
        return new DefaultValue(value);
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return SERVICE_PROVIDER.convert(clazz, value);
    }

    @Override
    public <T> List<T> getList(Class<T> clazz) {
        return (List<T>) value;
    }

    @Override
    public <T> Stream<T> getStream(Class<T> clazz) {
        return getList(clazz).stream();
    }

    @Override
    public <T> Set<T> getSet(Class<T> clazz) {
        return (Set<T>) value;
    }

    @Override
    public <K, V> Map<K, V> getSet(Class<K> keyClass, Class<V> valueClass) {
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
