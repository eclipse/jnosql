package org.apache.diana.api.key;


import java.io.Serializable;
import java.util.Optional;

public interface KeyValueEntityManager extends AutoCloseable {

    <T> void put(String key, T value);

    <T> void put(KeyValue keyValue);

    <T> void put(Iterable<KeyValue> keyValues);

    <K, V> Optional<V> get(K key, Class<V> entityClass);

    <K, V> Iterable<V> get(Iterable<K> keys, Class<V> entityClass);

    <K>  void remove(K key);

    <K> void remove(Iterable<K> keys);

}
