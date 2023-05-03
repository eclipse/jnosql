/*
 *
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.keyvalue;


import org.eclipse.jnosql.communication.Value;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Interface used to interact with the persistence context to {@link KeyValueEntity}
 * The BucketManager API is used to create and remove persistent {@link KeyValueEntity}.
 */
public interface BucketManager extends AutoCloseable {

    /**
     * Returns the bucket name of this {@link BucketManager}
     *
     * @return the bucket name
     */
    String name();

    /**
     * Associates the specified value with the specified key and then storage
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
     * @param entity the entity to be inserted
     * @throws NullPointerException when entity is null
     */
    void put(KeyValueEntity entity);

    /**
     * Saves the {@link KeyValueEntity} with time to live
     *
     * @param entity the entity to be inserted
     * @param ttl    the defined time to live
     * @throws NullPointerException          when entity is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    void put(KeyValueEntity entity, Duration ttl);

    /**
     * Saves the {@link Iterable} of keys
     *
     * @param entities keys to be inserted
     * @throws NullPointerException when the iterable is null
     */
    void put(Iterable<KeyValueEntity> entities);

    /**
     * Saves the {@link Iterable} of keys with a defined time to live
     *
     * @param entities keys to be inserted
     * @param ttl      the time to entity expire
     * @throws NullPointerException          when the iterable is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    void put(Iterable<KeyValueEntity> entities, Duration ttl);

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
    <K> void delete(K key);

    /**
     * Removes entities from keys
     *
     * @param keys the keys to be used
     * @param <K>  the key type
     * @throws NullPointerException when the key is null
     */
    <K> void delete(Iterable<K> keys);

    /**
     * Executes a query and returns the result, when the operations are <b>put</b>, <b>remove</b> and <b>get</b>
     * command it will return the result of the operation when the command is <b>remove</b> or <b>put </b>
     * it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link KeyValueQueryParser}
     */
    default Stream<Value> query(String query) {
        Objects.requireNonNull(query, "query is required");
        KeyValueQueryParser parser = new KeyValueQueryParser();
        return parser.query(query, this);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>put</b>, <b>remove</b> and <b>get</b>
     * command it will return the result of the operation when the command is either <b>remove</b> or <b>put</b>
     * it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link KeyValuePreparedStatement} instance
     * @throws NullPointerException  when there is parameter null
     * @throws IllegalStateException when there is not {@link KeyValuePreparedStatement}
     */
    default KeyValuePreparedStatement prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        KeyValueQueryParser parser = new KeyValueQueryParser();
        return parser.prepare(query, this);
    }

    /**
     * closes a resource
     */
    void close();

}
