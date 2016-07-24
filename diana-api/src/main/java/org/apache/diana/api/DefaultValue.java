package org.apache.diana.api;


import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.*;
import static java.util.stream.StreamSupport.stream;

final class DefaultValue implements Value {

    private static transient final ReaderField SERVICE_PROVIDER = ReaderFieldDecorator.getInstance();

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
    public <T> T cast() throws ClassCastException {
        return (T) value;
    }

    @Override
    public <T> T get(Class<T> clazz) {
        return SERVICE_PROVIDER.read(clazz, value);
    }

    @Override
    public <T> List<T> getList(Class<T> clazz) {
        if (Iterable.class.isInstance(value)) {
            Iterable iterable = Iterable.class.cast(value);
            return (List<T>) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(clazz, o)).collect(collectingAndThen(toList(), Collections::unmodifiableList));
        }
        return singletonList(get(clazz));
    }

    @Override
    public <T> Set<T> getSet(Class<T> clazz) {
        if (Iterable.class.isInstance(value)) {
            Iterable iterable = Iterable.class.cast(value);
            return (Set<T>) stream(iterable.spliterator(), false).map(o -> SERVICE_PROVIDER.read(clazz, o)).collect(collectingAndThen(toSet(), Collections::unmodifiableSet));
        }
        return Collections.singleton(get(clazz));
    }

    @Override
    public <K, V> Map<K, V> getMap(Class<K> keyClass, Class<V> valueClass) {

        if (Map.class.isInstance(value)) {
            Map mapValue = Map.class.cast(value);
            return (Map<K, V>) mapValue.keySet().stream().collect(Collectors.toMap(mapKeyElement(keyClass), mapValueElement(valueClass, mapValue)));
        }
        throw new UnsupportedOperationException("There is not supported convert" + value + " a not Map type.");
    }

    private <K> Function mapKeyElement(Class<K> keyClass) {
        return (keyElement) -> {
            if (SERVICE_PROVIDER.isCompatible(keyClass)) {
                return SERVICE_PROVIDER.read(keyClass, keyElement);
            }
            return keyElement;
        };
    }

    private <V> Function mapValueElement(Class<V> valueClass, Map mapValue) {
        return (keyElement) -> {
            Object valueElement = mapValue.get(keyElement);
            if (SERVICE_PROVIDER.isCompatible(valueClass)) {
                return SERVICE_PROVIDER.read(valueClass, valueElement);
            }
            return valueElement;
        };
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Value)) {
            return false;
        }
        Value that = (Value) o;
        return Objects.equals(value, that.get());
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
