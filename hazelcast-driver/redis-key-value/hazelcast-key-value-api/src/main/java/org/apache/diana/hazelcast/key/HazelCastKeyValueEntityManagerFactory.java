package org.apache.diana.hazelcast.key;


import com.hazelcast.core.HazelcastInstance;
import org.apache.diana.api.key.KeyValueEntityManager;
import org.apache.diana.api.key.KeyValueEntityManagerFactory;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class HazelCastKeyValueEntityManagerFactory implements KeyValueEntityManagerFactory {

    private final HazelcastInstance hazelcastInstance;

    HazelCastKeyValueEntityManagerFactory(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public KeyValueEntityManager getKeyValueEntityManager(String bucketName) {
        return new HazelCastKeyValueEntityManager(hazelcastInstance.getMap(bucketName));
    }

    @Override
    public <T> List<T> getList(String bucketName, Class<T> clazz) {
        return hazelcastInstance.getList(bucketName);
    }

    @Override
    public <T> Set<T> getSet(String bucketName, Class<T> clazz) {
        return hazelcastInstance.getSet(bucketName);
    }

    @Override
    public <T> Queue<T> getQueue(String bucketName, Class<T> clazz) {
        return hazelcastInstance.getQueue(bucketName);
    }

    @Override
    public <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) {
        return hazelcastInstance.getMap(bucketName);
    }

    @Override
    public void close() throws Exception {

    }
}
