/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */

package org.jnosql.diana.api.key;


import org.jnosql.diana.api.Value;

import java.time.Duration;
import java.util.Optional;

/**
 * Interface used to interact with the persistence context to {@link KeyValueEntity}
 * The BucketManager API is used to create and del persistent {@link KeyValueEntity}.
 *
 */
public interface BucketManager extends AutoCloseable {

    /**
     * Associates the specified value with the specified key and than storage
     *
     * @param key   the key
     * @param value the value
     * @param <K>   the key type
     * @param <V>   the value type
     * @throws NullPointerException when either key or value are null
     */
    <K, V> void put(K key, V value);

    /**
     * Saves the {@link KeyValueEntity}
     *
     * @param entity the entity to be insert
     * @param <K>      the key type
     * @throws NullPointerException when entity is null
     */
    <K> void put(KeyValueEntity<K> entity);

    /**
     * Saves the {@link KeyValueEntity} with time to live
     *
     * @param entity the entity to be insert
     * @param ttl      the defined time to live
     * @param <K>      the key type
     * @throws NullPointerException          when entity is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <K> void put(KeyValueEntity<K> entity, Duration ttl);

    /**
     * Saves the {@link Iterable} of keys
     *
     * @param entities keys to be insert
     * @param <K>       the key type
     * @throws NullPointerException when the iterable is null
     */
    <K> void put(Iterable<KeyValueEntity<K>> entities);

    /**
     * Saves the {@link Iterable} of keys with a defined time to live
     *
     * @param entities keys to be insert
     * @param ttl       the time to entity expire
     * @param <K>       the key type
     * @throws NullPointerException          when the iterable is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <K> void put(Iterable<KeyValueEntity<K>> entities, Duration ttl);

    /**
     * Finds the Value from a key
     *
     * @param key the key
     * @param <K> the key type
     * @return the {@link Optional} when is not found will return a {@link Optional#empty()}
     * @throws NullPointerException when the key is null
     */
    <K> Optional<Value> get(K key);

    /**
     * Finds a list of values from keys
     *
     * @param keys the keys to be used in this query
     * @param <K>  the key type
     * @return the list of result
     * @throws NullPointerException when either the keys or the entities values are null
     */
    <K> Iterable<Value> get(Iterable<K> keys);

    /**
     * Removes an entity from key
     *
     * @param key the key bo be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> void del(K key);

    /**
     * Removes entities from keys
     *
     * @param keys the keys to be used
     * @param <K>  the key type
     * @throws NullPointerException when the key is null
     */
    <K> void del(Iterable<K> keys);

    /**
     * closes a resource
     */
    void close();

}
