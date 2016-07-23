package org.apache.diana.api.key;


import java.io.Serializable;
import java.util.Optional;

public interface KeyValueEntityManager extends AutoCloseable {

    <T> void put(String key, T value);

    <T> void put(KeyValue keyValue);

    <T> void put(Iterable<KeyValue> keyValues);

    <T> Optional<T> get(String key, Class<T> entityClass);

    <T> Iterable<T> get(Iterable<String> key, Class<T> entityClass);

    void remove(String key);

    void remove(Iterable<String> keys);

}
