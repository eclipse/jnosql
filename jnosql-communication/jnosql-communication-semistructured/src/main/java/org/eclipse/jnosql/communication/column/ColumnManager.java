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
package org.eclipse.jnosql.communication.column;


import jakarta.data.exceptions.NonUniqueResultException;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The manager instance bridges the Jakarta NoSQL and the NoSQL vendor.
 *
 * @see ColumnEntity
 */
public interface ColumnManager extends AutoCloseable {

    /**
     * Returns the database's name of this {@link ColumnManager}
     *
     * @return the database's name
     */
    String name();

    /**
     * Saves a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity insert(ColumnEntity entity);

    /**
     * Updates a Column family entity
     *
     * @param entity column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entity is null
     */
    ColumnEntity update(ColumnEntity entity);

    /**
     * Updates a Column family entities, by default it's just run for each saving using
     * {@link ColumnManager#update(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    Iterable<ColumnEntity> update(Iterable<ColumnEntity> entities);

    /**
     * Saves a Column family entity with time to live
     *
     * @param entity column family to be saved
     * @param ttl    time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    ColumnEntity insert(ColumnEntity entity, Duration ttl);

    /**
     * Saves a Column family entities, by default it's just run for each saving using
     * {@link ColumnManager#insert(ColumnEntity)}, each NoSQL vendor might
     * replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities);

    /**
     * Saves a Column family entity with time to live, by default it's just run for each saving using
     * {@link ColumnManager#insert(ColumnEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities column family to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    Iterable<ColumnEntity> insert(Iterable<ColumnEntity> entities, Duration ttl);

    /**
     * Deletes an entity
     *
     * @param query the select to delete an entity
     * @throws NullPointerException          when either select or collection are null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(ColumnDeleteQuery query);

    /**
     * Finds {@link ColumnEntity} from select
     *
     * @param query - select to figure out entities
     * @return entities found by select
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    Stream<ColumnEntity> select(ColumnQuery query);

    /**
     * Returns the number of items in the column family that match a specified query.
     * @param query the query
     * @return the number of documents from query
     * @throws NullPointerException when query is null
     */
    default long count(ColumnQuery query) {
        Objects.requireNonNull(query, "query is required");
        return this.select(DefaultColumnQuery.countBy(query)).count();
    }

    /**
     * Returns whether an entity that match a specified query.
     * @param query the query
     * @return true if an entity with the given query exists, false otherwise.
     * @throws NullPointerException when query it null
     */
    default boolean exists(ColumnQuery query) {
        Objects.requireNonNull(query, "query is required");
        return this.select(DefaultColumnQuery.existsBy(query)).findAny().isPresent();
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link ColumnQueryParser}
     */
    default Stream<ColumnEntity> query(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParser parser = new ColumnQueryParser();
        return parser.query(query, this, ColumnObserverParser.EMPTY);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link ColumnPreparedStatement} instance
     * @throws NullPointerException  when there is parameter null
     * @throws IllegalStateException when there is not {@link ColumnQueryParser}
     */
    default ColumnPreparedStatement prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParser parser = new ColumnQueryParser();
        return parser.prepare(query, this, ColumnObserverParser.EMPTY);
    }

    /**
     * Returns a single entity from select
     *
     * @param query - select to figure out entities
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException      when the result has more than one entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    default Optional<ColumnEntity> singleResult(ColumnQuery query) {
        Objects.requireNonNull(query, "query is required");
        Stream<ColumnEntity> entities = select(query);
        final Iterator<ColumnEntity> iterator = entities.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final ColumnEntity entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

    /**
     * Returns the number of elements from column family
     *
     * @param columnFamily the column family
     * @return the number of elements
     * @throws NullPointerException          when column family is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    long count(String columnFamily);

    /**
     * closes a resource
     */
    void close();

}
