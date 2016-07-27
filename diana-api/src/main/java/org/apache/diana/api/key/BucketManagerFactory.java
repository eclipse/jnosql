package org.apache.diana.api.key;


import org.apache.diana.api.CloseResource;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * {@link BucketManager} factory.
 * When the application has finished using the bucket manager factory, and/or at application shutdown, the application should close the column family manager factory.
 *
 * @author Otávio Santana
 */
public interface BucketManagerFactory extends CloseResource {

    /**
     * Creates a {@link BucketManager} from a bucket name
     *
     * @param bucketName a bucket name
     * @return a {@link BucketManager} instance
     * @throws UnsupportedOperationException when the database does not have to it
     */
    BucketManager getBucketManager(String bucketName) throws UnsupportedOperationException;

    /**
     * Creates a {@link List} from bucket name
     *
     * @param bucketName a bucket name
     * @param clazz      the valeu class
     * @param <T>        the value type
     * @return a {@link List} instance
     * @throws UnsupportedOperationException when the database does not have to it
     */
    <T> List<T> getList(String bucketName, Class<T> clazz) throws UnsupportedOperationException;

    /**
     * Creates a {@link Set} from bucket name
     *
     * @param bucketName a bucket name
     * @param clazz      the valeu class
     * @param <T>        the value type
     * @return a {@link Set} instance
     * @throws UnsupportedOperationException when the database does not have to it
     */
    <T> Set<T> getSet(String bucketName, Class<T> clazz) throws UnsupportedOperationException;

    /**
     * Creates a {@link Queue} from bucket name
     *
     * @param bucketName a bucket name
     * @param clazz      the valeu class
     * @param <T>        the value type
     * @return a {@link Queue} instance
     * @throws UnsupportedOperationException when the database does not have to it
     */
    <T> Queue<T> getQueue(String bucketName, Class<T> clazz) throws UnsupportedOperationException;

    /**
     * Creates a {@link  Map} from bucket name
     *
     * @param bucketName the bucket name
     * @param keyValue   the key class
     * @param valueValue the value class
     * @param <K>        the key type
     * @param <V>        the value type
     * @return a {@link Map} instance
     * @throws UnsupportedOperationException when the database does not have to it
     */
    <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) throws UnsupportedOperationException;
}
