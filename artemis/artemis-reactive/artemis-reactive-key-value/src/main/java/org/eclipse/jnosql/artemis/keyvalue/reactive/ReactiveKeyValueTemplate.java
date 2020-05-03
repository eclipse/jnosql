/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.artemis.keyvalue.reactive;

import org.reactivestreams.Publisher;

import java.time.Duration;
import java.util.Optional;

/**
 * The {@link jakarta.nosql.mapping.keyvalue.KeyValueTemplate} wrapper to the reactive API.
 */
public interface ReactiveKeyValueTemplate {

    /**
     * Saves the entity
     *
     * @param entity the entity to be insert
     * @param <T>    the entity type
     * @return the entity
     * @throws NullPointerException when entity is null
     */
    <T> Publisher<T> put(T entity);

    /**
     * Saves the entity with time to live
     *
     * @param entity the entity to be insert
     * @param ttl    the defined time to live
     * @param <T>    the entity type
     * @return the entity
     * @throws NullPointerException          when entity is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <T> Publisher<T> put(T entity, Duration ttl);

    /**
     * Saves the {@link Iterable} of entities
     *
     * @param entities keys to be insert
     * @param <T>      the entity type
     * @return the entities
     * @throws NullPointerException when the iterable is null
     */
    <T> Publisher<T> put(Iterable<T> entities);

    /**
     * Saves the {@link Iterable} of entities with a defined time to live
     *
     * @param entities entities to be insert
     * @param ttl      the time to entity expire
     * @param <T>      the entity type
     * @return the entities
     * @throws NullPointerException          when the iterable is null
     * @throws UnsupportedOperationException when expired time is not supported
     */
    <T> Publisher<T> put(Iterable<T> entities, Duration ttl);

    /**
     * Finds the Value from a key
     *
     * @param key         the key
     * @param <K>         the key type
     * @param <T>         the entity type
     * @param entityClass the entity class to convert the result
     * @return the {@link Optional} when is not found will return a {@link Optional#empty()}
     * @throws NullPointerException when the key is null
     */
    <K, T> Publisher<T> get(K key, Class<T> entityClass);

    /**
     * Executes query in the database
     *
     * @param query       the query
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return the result list, if either <b>put</b> or <b>remove</b> it will return empty
     * @throws NullPointerException when query is null, if the query is <b>get</b> the entity class is required
     */
    <T> Publisher<T> query(String query, Class<T> entityClass);

    /**
     * Executes query in the database then returns as single result
     *
     * @param query       the query
     * @param entityClass the entity class
     * @param <T>         the entity type
     * @return the result {@link Optional}, if either <b>put</b> or <b>remove</b> it will return {@link Optional#empty()}
     * @throws NullPointerException when query is null, if the query is <b>get</b> the entity class is required
     */
    <T> Publisher<T> getSingleResult(String query, Class<T> entityClass);

    /**
     * Executes query in the database and don't return result, e.g.: when the query is either <b>remove</b> or
     * <b>put</b>
     *
     * @param query the query
     * @throws NullPointerException when query is null
     */
    Publisher<Void> query(String query);


    /**
     * Finds a list of values from keys
     *
     * @param entityClass the entity class
     * @param keys        the keys to be used in this query
     * @param <K>         the key type
     * @param <T>         the entity type
     * @return the list of result
     * @throws NullPointerException when either the keys or the entities values are null
     */
    <K, T> Publisher<T> get(Iterable<K> keys, Class<T> entityClass);

    /**
     * Removes an entity from key
     *
     * @param key the key bo be used
     * @param <K> the key type
     * @throws NullPointerException when the key is null
     */
    <K> Publisher<Void> delete(K key);

    /**
     * Removes entities from keys
     *
     * @param keys the keys to be used
     * @param <K>  the key type
     * @throws NullPointerException when the key is null
     */
    <K> Publisher<Void> delete(Iterable<K> keys);

}
