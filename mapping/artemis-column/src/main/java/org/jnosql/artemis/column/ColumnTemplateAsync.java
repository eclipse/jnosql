/*
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
 */
package org.jnosql.artemis.column;


import org.jnosql.artemis.PreparedStatementAsync;
import org.jnosql.diana.api.NonUniqueResultException;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnQuery;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

/**
 * This interface that represents the common operation between an entity
 * and {@link org.jnosql.diana.api.column.ColumnEntity} to do async operations
 *
 * @see org.jnosql.diana.api.column.ColumnFamilyManagerAsync
 */
public interface ColumnTemplateAsync {

    /**
     * Inserts an entity asynchronously
     *
     * @param entity entity to be saved
     * @param <T>    the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when entity is null
     */
    <T> void insert(T entity);

    /**
     * Inserts an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @param <T>    the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either entity or ttl are null
     */
    <T> void insert(T entity, Duration ttl);

    /**
     * Inserts entities asynchronously, by default it's just run for each saving using
     * {@link ColumnTemplate#insert(Object)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when entities is null
     */
    default <T> void insert(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::insert);
    }

    /**
     * Inserts entities asynchronously with time to live, by default it's just run for each saving using
     * {@link ColumnTemplate#insert(Object, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param ttl      time to live
     * @param <T>      the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either entities or ttl are null
     */
    default <T> void insert(Iterable<T> entities, Duration ttl) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(d -> insert(d, ttl));
    }

    /**
     * Inserts an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callback the callback, when the process is finished will call this instance returning
     *                 the saved entity within parameters
     * @param <T>      the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either entity or callback are null
     */
    <T> void insert(T entity, Consumer<T> callback);


    /**
     * Inserts an entity asynchronously with time to live
     *
     * @param entity   entity to be saved
     * @param ttl      time to live
     * @param callback the callback, when the process is finished will call this instance returning
     *                 the saved entity within parameters
     * @param <T>      the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either entity or ttl or callback are null
     */
    <T> void insert(T entity, Duration ttl, Consumer<T> callback);

    /**
     * Inserts an entity asynchronously
     *
     * @param entity entity to be updated
     * @param <T>    the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when entity is null
     */
    <T> void update(T entity);

    /**
     * Inserts entities asynchronously, by default it's just run for each saving using
     * {@link ColumnTemplate#update(Object)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when entities is null
     */
    default <T> void update(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::update);
    }

    /**
     * Inserts an entity asynchronously
     *
     * @param entity   entity to be updated
     * @param callback the callback, when the process is finished will call this instance returning
     *                 the updated entity within parametersa
     * @param <T>      the instance type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either entity or callback are null
     */
    <T> void update(T entity, Consumer<T> callback);

    /**
     * Deletes an entity asynchronously
     *
     * @param query query to delete an entity
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when query are null
     */
    void delete(ColumnDeleteQuery query);

    /**
     * Deletes an entity asynchronously
     *
     * @param query    query to delete an entity
     * @param callback the callback, when the process is finished will call this instance returning
     *                 the null within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to delete asynchronous
     * @throws NullPointerException                            when either query or callback are null
     */
    void delete(ColumnDeleteQuery query, Consumer<Void> callback);

    /**
     * Finds entities from query asynchronously
     *
     * @param query    query to select entities
     * @param <T>      the instance type
     * @param callback the callback, when the process is finished will call this instance returning
     *                 the result of query within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either query or callback are null
     */
    <T> void select(ColumnQuery query, Consumer<List<T>> callback);

    /**
     * Executes a query then bring the result as a {@link List}
     *
     * @param callback the callback, when the process is finished will call this instance returning
     * @param query    the query
     * @param <T>      the entity type
     * @throws NullPointerException when the query is null
     */
    <T> void query(String query, Consumer<List<T>> callback);

    /**
     * Executes a query then bring the result as a unique result
     *
     * @param callback the callback, when the process is finished will call this instance returning
     * @param query    the query
     * @param <T>      the entity type
     * @throws NullPointerException     when the query is null
     * @throws NonUniqueResultException if returns more than one result
     */
    <T> void singleResult(String query, Consumer<Optional<T>> callback);

    /**
     * Creates a {@link PreparedStatementAsync} from the query
     *
     * @param query the query
     * @return a {@link PreparedStatementAsync} instance
     * @throws NullPointerException when the query is null
     */
    PreparedStatementAsync prepare(String query);

    /**
     * Finds by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>        the id type
     * @param callback    the callback observer
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws org.jnosql.artemis.IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> void find(Class<T> entityClass, K id, Consumer<Optional<T>> callback);

    /**
     * Deletes by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>        the id type
     * @param callback    the callback
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws org.jnosql.artemis.IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> void delete(Class<T> entityClass, K id, Consumer<Void> callback);


    /**
     * Deletes by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>        the id type
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws org.jnosql.artemis.IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> void delete(Class<T> entityClass, K id);

    /**
     * Returns the number of elements from column family
     *
     * @param columnFamily the column family
     * @param callback     the callback with the response
     * @throws NullPointerException          when there is null parameter
     * @throws UnsupportedOperationException when the database dot not have support
     */
    void count(String columnFamily, Consumer<Long> callback);

    /**
     * Returns the number of elements from column family
     *
     * @param <T> the entity type
     * @param entityClass the entity class
     * @param callback     the callback with the response
     * @throws NullPointerException          when there is null parameter
     * @throws UnsupportedOperationException when the database dot not have support
     */
    <T> void count(Class<T> entityClass, Consumer<Long> callback);

    /**
     * Execute a query to consume an unique result
     *
     * @param query    the query
     * @param callback the callback
     * @param <T>      the type
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException when there is a async error
     * @throws UnsupportedOperationException                   when the database does not have support to insert asynchronous
     * @throws NullPointerException                            when either query or callback are null
     * @throws NonUniqueResultException                        when it returns more than one result
     */
    default <T> void singleResult(ColumnQuery query, Consumer<Optional<T>> callback) {

        requireNonNull(callback, "callback is required");

        Consumer<List<T>> singleCallBack = entities -> {
            if (entities.isEmpty()) {
                callback.accept(Optional.empty());
            } else if (entities.size() == 1) {
                callback.accept(Optional.of(entities.get(0)));
            } else {
                throw new NonUniqueResultException("The query returns more than one entity, query: " + query);
            }
        };
        select(query, singleCallBack);

    }
}
