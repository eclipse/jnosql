package org.apache.diana.redis.key;

import com.google.gson.Gson;
import org.apache.diana.api.key.KeyValueEntityManager;
import org.apache.diana.api.key.KeyValueEntityManagerFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

class RedisKeyValueEntityManagerFactory implements KeyValueEntityManagerFactory {

    private final Jedis jedis;

    RedisKeyValueEntityManagerFactory(Jedis jedis) {
        this.jedis = jedis;
    }


    @Override
    public KeyValueEntityManager getKeyValueEntity(String bucketName) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        return new RedisKeyValueEntityManager(bucketName, new Gson(), jedis);
    }

    @Override
    public <T> List<T> getList(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisList<T>(jedis, clazz, bucketName);
    }

    @Override
    public <T> Set<T> getSet(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisSet<T>(jedis, clazz, bucketName);
    }

    @Override
    public <T> Queue<T> getQueue(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisQueue<T>(jedis, clazz, bucketName);
    }

    @Override
    public <T> Map<String, T> getMap(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisMap<T>(jedis, clazz, bucketName);
    }

    @Override
    public void close() throws Exception {
        jedis.close();
    }
}
