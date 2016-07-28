package org.apache.diana.redis.key;


import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.diana.api.Value;
import org.apache.diana.api.key.BucketManager;
import org.apache.diana.api.key.KeyValue;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static org.apache.diana.redis.key.RedisUtils.createKeyWithNameSpace;

class RedisKeyValueEntityManager implements BucketManager {

    private final String nameSpace;

    private final Gson gson;

    private final Jedis jedis;

    RedisKeyValueEntityManager(String nameSpace, Gson gson, Jedis jedis) {
        this.nameSpace = nameSpace;
        this.gson = gson;
        this.jedis = jedis;
    }

    @Override
    public <K, V> void put(K key, V value) throws NullPointerException {
        Objects.requireNonNull(value, "Value is required");
        Objects.requireNonNull(key, "key is required");
        String valideKey = createKeyWithNameSpace(key.toString(), nameSpace);
        jedis.set(valideKey, gson.toJson(value));
    }

    @Override
    public <K> void put(KeyValue<K> keyValue) throws NullPointerException {
        put(keyValue.getKey(), keyValue.getValue().get());
    }

    @Override
    public <K> void put(Iterable<KeyValue<K>> keyValues) throws NullPointerException {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
    }

    @Override
    public <K> Optional<Value> get(K key) throws NullPointerException {
        String value = jedis.get(createKeyWithNameSpace(key.toString(), nameSpace));
        if (StringUtils.isNotBlank(value)) {
            return Optional.of(RedisValue.of(gson, value));
        }
        return Optional.empty();
    }

    @Override
    public <K> Iterable<Value> get(Iterable<K> keys) throws NullPointerException {
        return StreamSupport.stream(keys.spliterator(), false).map(k -> jedis.get(createKeyWithNameSpace(k.toString(), nameSpace))).
                filter(StringUtils::isNotBlank).map(v -> RedisValue.of(gson, v)).collect(toList());
    }

    @Override
    public <K> void remove(K key) {
        jedis.del(createKeyWithNameSpace(key.toString(), nameSpace));
    }

    @Override
    public <K> void remove(Iterable<K> keys) {
        StreamSupport.stream(keys.spliterator(), false).forEach(this::remove);
    }

    @Override
    public void close() {
        jedis.close();
    }
}
