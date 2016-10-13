/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.column;


import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.jnosql.diana.api.CloseResource;
import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.NonUniqueResultException;
import org.jnosql.diana.api.TTL;

/**
 * Interface used to interact with the persistence context to {@link ColumnEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnEntity} instances,
 * to find entities by their primary key, and to query over entities.
 */
public interface ColumnFamilyManager extends CloseResource {

    /**
     * Saves a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity save(ColumnEntity entity) throws NullPointerException;

    /**
     * Saves a Column family entity with time to live
     *
     * @param entity column family to be saved
     * @param ttl    time to live
     * @return the entity saved
     * @throws NullPointerException when either entity or ttl are null
     */
    ColumnEntity save(ColumnEntity entity, TTL ttl) throws NullPointerException;

    /**
     * Saves an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnEntity entity, TTL ttl) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnEntity entity, Consumer<ColumnEntity> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Saves a Column family entities, by default it's just run for each saving using
     * {@link ColumnFamilyManager#save(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default Iterable<ColumnEntity> save(Iterable<ColumnEntity> entities) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::save).collect(Collectors.toList());
    }

    /**
     * Saves a Column family entity with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManager#save(ColumnEntity, TTL)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException when either entity or ttl are null
     */
    default Iterable<ColumnEntity> save(Iterable<ColumnEntity> entities, TTL ttl) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(c -> this.save(c, ttl)).collect(Collectors.toList());
    }

    /**
     * Saves an entities asynchronously, by default it's just run for each saving using
     * {@link ColumnFamilyManager#saveAsync(ColumnEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    default void saveAsync(Iterable<ColumnEntity> entities) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::save);
    }

    /**
     * Saves an entities asynchronously with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManager#saveAsync(ColumnEntity, TTL)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @param ttl      time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    default void saveAsync(Iterable<ColumnEntity> entities, TTL ttl) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(c -> this.save(c, ttl));
    }


    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param ttl      time to live
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnEntity entity, TTL ttl, Consumer<ColumnEntity> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Updates a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity updated
     */
    ColumnEntity update(ColumnEntity entity);

    /**
     * Updates an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to update asynchronous
     */
    void updateAsync(ColumnEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Updates an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the updated entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to update asynchronous
     */
    void updateAsync(ColumnEntity entity, Consumer<ColumnEntity> callBack) throws
            ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     */
    void delete(ColumnQuery query);

    /**
     * Deletes an entity asynchronously
     *
     * @param query query to delete an entity
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to delete asynchronous
     */
    void deleteAsync(ColumnQuery query) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Deletes an entity asynchronously
     *
     * @param query    query to delete an entity
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the null within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to delete asynchronous
     */
    void deleteAsync(ColumnQuery query, Consumer<Void> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Finds {@link ColumnEntity} from query
     *
     * @param query - query to figure out entities
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    List<ColumnEntity> find(ColumnQuery query) throws NullPointerException;

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException  when query is null
     */
    default Optional<ColumnEntity> singleResult(ColumnQuery query) throws NonUniqueResultException, NullPointerException {
        List<ColumnEntity> entities = find(query);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The query returns more than one entity, query: " + query);
    }

    /**
     * Finds {@link ColumnEntity} from query asynchronously
     *
     * @param query    query to find entities
     * @param callBack the callback, when the process is finished will call this instance returning the
     *                 result of query within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void findAsync(ColumnQuery query, Consumer<List<ColumnEntity>> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

}
