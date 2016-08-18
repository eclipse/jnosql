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


import org.jnosql.diana.api.CloseResource;
import org.jnosql.diana.api.ExecuteAsyncQueryException;
import org.jnosql.diana.api.TTL;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Interface used to interact with the persistence context to {@link ColumnFamilyEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnFamilyEntity} instances,
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
    ColumnFamilyEntity save(ColumnFamilyEntity entity) throws NullPointerException;

    /**
     * Saves a Column family entity with time to live
     *
     * @param entity column family to be saved
     * @param ttl    time to live
     * @return the entity saved
     * @throws NullPointerException when either entity or ttl are null
     */
    ColumnFamilyEntity save(ColumnFamilyEntity entity, TTL ttl) throws NullPointerException;

    /**
     * Saves an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnFamilyEntity entity, TTL ttl) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void saveAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Saves a Column family entities, by default it's just run for each saving using
     * {@link ColumnFamilyManager#save(ColumnFamilyEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default Iterable<ColumnFamilyEntity> save(Iterable<ColumnFamilyEntity> entities) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::save).collect(Collectors.toList());
    }

    /**
     * Saves a Column family entity with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManager#save(ColumnFamilyEntity, TTL)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException when either entity or ttl are null
     */
    default Iterable<ColumnFamilyEntity> save(Iterable<ColumnFamilyEntity> entities, TTL ttl) throws NullPointerException {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(c -> this.save(c, ttl)).collect(Collectors.toList());
    }

    /**
     * Saves an entities asynchronously, by default it's just run for each saving using
     * {@link ColumnFamilyManager#saveAsync(ColumnFamilyEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    default void saveAsync(Iterable<ColumnFamilyEntity> entities) throws ExecuteAsyncQueryException, UnsupportedOperationException {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::save);
    }

    /**
     * Saves an entities asynchronously with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManager#saveAsync(ColumnFamilyEntity, TTL)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @param ttl      time to live
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    default void saveAsync(Iterable<ColumnFamilyEntity> entities, TTL ttl) throws ExecuteAsyncQueryException, UnsupportedOperationException {
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
    void saveAsync(ColumnFamilyEntity entity, TTL ttl, Consumer<ColumnFamilyEntity> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Updates a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity updated
     */
    ColumnFamilyEntity update(ColumnFamilyEntity entity);

    /**
     * Updates an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to update asynchronous
     */
    void updateAsync(ColumnFamilyEntity entity) throws ExecuteAsyncQueryException, UnsupportedOperationException;

    /**
     * Updates an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the updated entity within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to update asynchronous
     */
    void updateAsync(ColumnFamilyEntity entity, Consumer<ColumnFamilyEntity> callBack) throws
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
     * Finds {@link ColumnFamilyEntity} from query
     *
     * @param query - query to figure out entities
     * @return entities found by query
     */
    List<ColumnFamilyEntity> find(ColumnQuery query);

    /**
     * Finds {@link ColumnFamilyEntity} from query asynchronously
     *
     * @param query    query to find entities
     * @param callBack the callback, when the process is finished will call this instance returning the
     *                 result of query within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to save asynchronous
     */
    void findAsync(ColumnQuery query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Executes a native query from database, this query may be difference between kind of database.
     *
     * @param query query to be executed
     * @return the result of query
     * @throws UnsupportedOperationException when the database does not have support to run native query
     */
    List<ColumnFamilyEntity> nativeQuery(String query) throws UnsupportedOperationException;

    /**
     * Executes a native query from database, this query may be difference between
     * kind of database and run it asynchronously.
     *
     * @param query    query to be executed
     * @param callBack the callback, when the process is finished will call this instance
     *                 returning the result of query within parameters
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not have support to run native query async.
     */
    void nativeQueryAsync(String query, Consumer<List<ColumnFamilyEntity>> callBack) throws ExecuteAsyncQueryException,
            UnsupportedOperationException;

    /**
     * Creates a {@link PreparedStatement} from a native query
     *
     * @param query a query to be executed
     * @return a {@link PreparedStatement}
     * @throws UnsupportedOperationException the there is not support to query
     */
    PreparedStatement nativeQueryPrepare(String query) throws UnsupportedOperationException;

    /**
     * Finds {@link ColumnFamilyEntity} from a Diana Query Language
     *
     * @param query Diana query language
     * @return entities from query
     * @throws NullPointerException when query is null
     */
    default List<ColumnFamilyEntity> query(String query) throws NullPointerException {
        Objects.requireNonNull(query, "query is required");
        return Collections.emptyList();
    }

    /**
     * Inserts or updates column families from Diana Query Language
     *
     * @param query Diana query language
     * @throws ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support async save
     */
    default void insert(String query) throws NullPointerException, ExecuteAsyncQueryException, UnsupportedOperationException {
        Objects.requireNonNull(query, "query is required");
    }

}
