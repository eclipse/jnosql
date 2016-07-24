package org.apache.diana.api.key;


import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public interface BucketManagerFactory extends AutoCloseable {

    BucketManager getBucketManager(String bucketName);

    <T> List<T> getList(String bucketName, Class<T> clazz);

    <T> Set<T> getSet(String bucketName, Class<T> clazz);

    <T> Queue<T> getQueue(String bucketName, Class<T> clazz);

    <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue);
}
