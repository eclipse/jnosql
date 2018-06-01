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

package org.jnosql.diana.api.column;


import org.jnosql.diana.api.NonUniqueResultException;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

/**
 * Interface used to interact with the persistence context to {@link ColumnEntity}
 * The ColumnFamilyManager API is used to create and remove persistent {@link ColumnEntity} instances,
 * to select entities by their primary key, and to select over entities. The main difference to {@link ColumnFamilyManager}
 * is because all the operation works asynchronously.
 */
public interface ColumnFamilyManagerAsync extends AutoCloseable {


    /**
     * Saves an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void insert(ColumnEntity entity);

    /**
     * Saves an entity asynchronously with time to live
     *
     * @param entity entity to be saved
     * @param ttl    time to live
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when the entity is null
     */
    void insert(ColumnEntity entity, Duration ttl);

    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or callback are null
     */
    void insert(ColumnEntity entity, Consumer<ColumnEntity> callBack);


    /**
     * Saves an entities asynchronously, by default it's just run for each saving using
     * {@link ColumnFamilyManagerAsync#insert(ColumnEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    default void insert(Iterable<ColumnEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::insert);
    }

    /**
     * Saves an entities asynchronously with time to live, by default it's just run for each saving using
     * {@link ColumnFamilyManagerAsync#insert(ColumnEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @param ttl      time to live
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entities or ttl are null
     */
    default void insert(Iterable<ColumnEntity> entities, Duration ttl) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(c -> this.insert(c, ttl));
    }


    /**
     * Saves an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param ttl      time to live
     * @param callBack the callback, when the process is finished will call this instance returning the saved entity
     *                 within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity or ttl or callback are null
     */
    void insert(ColumnEntity entity, Duration ttl, Consumer<ColumnEntity> callBack);


    /**
     * Updates an entity asynchronously
     *
     * @param entity entity to be saved
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entity is null
     */
    void update(ColumnEntity entity);

    /**
     * Updates an entities asynchronously, by default it's just run for each saving using
     * {@link ColumnFamilyManagerAsync#update(ColumnEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entity to be saved
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when entities is null
     */
    default void update(Iterable<ColumnEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        StreamSupport.stream(entities.spliterator(), false).forEach(this::update);
    }

    /**
     * Updates an entity asynchronously
     *
     * @param entity   entity to be saved
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the updated entity within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either entity, callback are null
     */
    void update(ColumnEntity entity, Consumer<ColumnEntity> callBack);


    /**
     * Deletes an entity asynchronously
     *
     * @param query select to delete an entity
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when select is null
     */
    void delete(ColumnDeleteQuery query);

    /**
     * Deletes an entity asynchronously
     *
     * @param query    select to delete an entity
     * @param callBack the callback, when the process is finished will call this instance returning
     *                 the null within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either select or callback are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(ColumnDeleteQuery query, Consumer<Void> callBack);


    /**
     * Finds {@link ColumnEntity} from select asynchronously
     *
     * @param query    select to select entities
     * @param callBack the callback, when the process is finished will call this instance returning the
     *                 result of select within parameters
     * @throws org.jnosql.diana.api.ExecuteAsyncQueryException    when there is a async error
     * @throws UnsupportedOperationException when the database does not support this feature
     * @throws NullPointerException          when either select or callback are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void select(ColumnQuery query, Consumer<List<ColumnEntity>> callBack);

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param callBack the callback result
     * @param query    the query as {@link String}
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalArgumentException        when the query has value parameters
     * @throws IllegalStateException           when there is not {@link ColumnQueryParserAsync}
     * @throws org.jnosql.query.QueryException when there is error in the syntax
     */
    default void query(String query, Consumer<List<ColumnEntity>> callBack) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(callBack, "callBack is required");
        ColumnQueryParserAsync parser = ColumnQueryParserAsync.getParser();
        parser.query(query, this, callBack);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link ColumnPreparedStatementAsync} instance
     * @throws NullPointerException            when there is parameter null
     * @throws IllegalStateException           when there is not {@link ColumnQueryParserAsync}
     * @throws org.jnosql.query.QueryException when there is error in the syntax
     */
    default ColumnPreparedStatementAsync prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParserAsync parser = ColumnQueryParserAsync.getParser();
        return parser.prepare(query, this);
    }


    /**
     * Returns a single entity from select
     *
     * @param query    - select to figure out entities
     * @param callBack the callback
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    default void singleResult(ColumnQuery query, Consumer<Optional<ColumnEntity>> callBack) {
        select(query, entities -> {
            if (entities.isEmpty()) {
                callBack.accept(Optional.empty());
                return;
            } else if (entities.size() == 1) {
                callBack.accept(Optional.of(entities.get(0)));
                return;
            }
            throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
        });

    }

    /**
     * closes a resource
     */
    void close();

}
