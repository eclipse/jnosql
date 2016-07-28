package org.apache.diana.hazelcast.key;

import org.apache.diana.api.key.BucketManager;
import org.apache.diana.api.key.KeyValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;


class HazelCastKeyValueEntityManager implements BucketManager {

    private final Map map;

    HazelCastKeyValueEntityManager(Map map) {
        this.map = map;
    }

    @Override
    public <K, V> void put(K key, V value) {
        map.put(key, value);
    }

    @Override
    public <T> void put(KeyValue keyValue) {
        map.put(keyValue.getKey(), keyValue.getValue().get());
    }

    @Override
    public <T> void put(Iterable<KeyValue> keyValues) {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
    }

    @Override
    public <K, V> Optional<V> get(K key, Class<V> entityClass) {
        V value = (V) map.get(key);
        return Optional.ofNullable(value);
    }

    @Override
    public <K, V> Iterable<V> get(Iterable<K> keys, Class<V> entityClass) {

        List<V> list = new ArrayList<V>();
        for (K key : keys) {
            Object value = map.get(key);
            if (value != null) {
                list.add((V) value);
            }
        }
        return list;
    }

    @Override
    public <K>  void remove(K key) {
        map.remove(key);
    }

    @Override
    public <K> void remove(Iterable<K> keys) {
        StreamSupport.stream(keys.spliterator(), false).forEach(this::remove);
    }

    @Override
    public void close()  {
    }
}
