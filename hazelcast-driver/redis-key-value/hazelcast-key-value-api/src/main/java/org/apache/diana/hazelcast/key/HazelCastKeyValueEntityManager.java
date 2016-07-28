package org.apache.diana.hazelcast.key;

import org.apache.diana.api.Value;
import org.apache.diana.api.key.BucketManager;
import org.apache.diana.api.key.KeyValue;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public <K> void put(KeyValue<K> keyValue) throws NullPointerException {
        map.put(keyValue.getKey(), keyValue.getValue().get());
    }

    @Override
    public <K> void put(Iterable<KeyValue<K>> keyValues) throws NullPointerException {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
    }

    @Override
    public <K> Optional<Value> get(K key) throws NullPointerException {
        Object value = map.get(key);
        if(value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(Value.of(value));
    }

    @Override
    public <K> Iterable<Value> get(Iterable<K> keys) throws NullPointerException {
        return StreamSupport.stream(keys.spliterator(), false).map(k -> map.get(k)).filter(Objects::nonNull).map(Value::of).collect(Collectors.toList());
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
