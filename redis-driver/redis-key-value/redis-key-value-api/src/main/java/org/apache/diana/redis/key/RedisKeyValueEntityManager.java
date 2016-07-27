package org.apache.diana.redis.key;


import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
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
    public <T> void put(String key, T value) {
        Objects.requireNonNull(value, "Value is required");
        Objects.requireNonNull(key, "key is required");
        String valideKey = createKeyWithNameSpace(key, nameSpace);
        jedis.set(valideKey, gson.toJson(value));
    }

    @Override
    public <T> void put(KeyValue keyValue) {
        put(keyValue.getKey(), keyValue.getValue().get());
    }

    @Override
    public <T> void put(Iterable<KeyValue> keyValues) {
        StreamSupport.stream(keyValues.spliterator(), false).forEach(this::put);
    }

    @Override
    public <K, V> Optional<V> get(K key, Class<V> entityClass) {
        String value = jedis.get(createKeyWithNameSpace(key.toString(), nameSpace));
        if (StringUtils.isNotBlank(value)) {
            return Optional.of(gson.fromJson(value, entityClass));
        }
        return Optional.empty();
    }

    @Override
    public <K, V> Iterable<V> get(Iterable<K> keys, Class<V> entityClass) {
        return StreamSupport.stream(keys.spliterator(), false).map(k -> jedis.get(createKeyWithNameSpace(k.toString(), nameSpace))).
                filter(StringUtils::isNotBlank).map(v -> gson.fromJson(v, entityClass)).collect(toList());
    }

    @Override
    public<K>  void remove(K key) {
        jedis.del(createKeyWithNameSpace(key.toString(), nameSpace));
    }

    @Override
    public <K> void remove(Iterable<K> keys) {
        StreamSupport.stream(keys.spliterator(), false).forEach(this::remove);
    }

    @Override
    public void close()  {
        jedis.close();
    }
}
