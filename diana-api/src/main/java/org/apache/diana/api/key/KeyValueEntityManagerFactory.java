package org.apache.diana.api.key;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public interface KeyValueEntityManagerFactory {

    KeyValueEntityManager getKeyValueEntity();

    <T extends Serializable> List<T> getList(String bucketName);

    <T extends Serializable> Set<T> getSet(String bucketName);

    <T extends Serializable> Queue<T> getQueue(String bucketName);

    <T extends Serializable> Map<String, T> getMap(String bucketName);
}
