package org.apache.diana.api.key;


import org.apache.diana.api.CloseResource;

import java.util.Optional;

/**
 * Interface used to interact with the persistence context to {@link KeyValue}
 * The BucketManager API is used to create and remove persistent {@link KeyValue}.
 *
 * @author Otávio Santana
 */
public interface BucketManager extends CloseResource {

    /**
     * Associates the specified value with the specified key and than storage
     *
     * @param key   the key
     * @param value the value
     * @param <K>   the key type
     * @param <V>   the value type
     * @throws NullPointerException when either key or value are null
     */
    <K, V> void put(K key, V value) throws NullPointerException;

    /**
     * Saves the {@link KeyValue}
     * @param keyValue the entity to be save
     * @param <K> the key type
     * @throws NullPointerException when entity is null
     */
    <K> void put(KeyValue<K> keyValue) throws NullPointerException;

    /**
     * Saves the {@link Iterable} of keys
     * @param keyValues keys to be save
     * @param <K> the key type
     * @throws NullPointerException when the iterable is null
     */
    <K> void put(Iterable<KeyValue<K>> keyValues) throws NullPointerException;

    /**
     * Finds the Value from a key
     * @param key the key
     * @param entityClass the value type
     * @param <K> the key type
     * @param <V> the value type
     * @return the {@link Optional} when is not found will return a {@link Optional#empty()}
     * @throws NullPointerException
     */
    <K, V> Optional<V> get(K key, Class<V> entityClass) throws NullPointerException;

    /**
     * Finds a list of values from keys
     * @param keys the keys to be used in this query
     * @param entityClass the value type
     * @param <K> the key type
     * @param <V> the value type
     * @return the list of result
     * @throws NullPointerException when either the keys or the entities values are null
     */
    <K, V> Iterable<V> get(Iterable<K> keys, Class<V> entityClass) throws NullPointerException;

    /**
     * Removes an entity from key
     * @param key the key bo be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> void remove(K key) throws NullPointerException;

    /**
     * Removes entities from keys
     * @param keys the keys to be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> void remove(Iterable<K> keys)throws NullPointerException;

}
