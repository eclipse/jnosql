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
package org.eclipse.jnosql.communication.semistructured;


import jakarta.data.exceptions.NonUniqueResultException;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * The manager instance bridges between Jakarta NoSQL and the NoSQL vendor, providing operations
 * to interact with a database.
 *
 * @see CommunicationEntity
 */
public interface DatabaseManager extends AutoCloseable {

    /**
     * Returns the name of the database managed by this {@link DatabaseManager}.
     *
     * @return the name of the database
     */
    String name();

    /**
     * Saves an entity into the database.
     *
     * @param entity the entity to be saved
     * @return the saved entity
     * @throws NullPointerException when the provided entity is null
     */
    CommunicationEntity insert(CommunicationEntity entity);

    /**
     * Updates an entity in the database.
     *
     * @param entity the entity to be updated
     * @return the updated entity
     * @throws NullPointerException when the provided entity is null
     */
    CommunicationEntity update(CommunicationEntity entity);

    /**
     * Updates multiple entities in the database. By default, each entity is updated
     * individually using {@link DatabaseManager#update(CommunicationEntity)}. Each NoSQL vendor may
     * provide a more appropriate implementation.
     *
     * @param entities the entities to be updated
     * @return the updated entities
     * @throws NullPointerException when the provided collection of entities is null
     */
    Iterable<CommunicationEntity> update(Iterable<CommunicationEntity> entities);

    /**
     * Saves an entity into the database with a specified time to live (TTL).
     *
     * @param entity the entity to be saved
     * @param ttl    the time to live for the entity
     * @return the saved entity
     * @throws NullPointerException          when either the entity or the TTL is null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    CommunicationEntity insert(CommunicationEntity entity, Duration ttl);

    /**
     * Saves multiple entities into the database. By default, each entity is saved
     * individually using {@link DatabaseManager#insert(CommunicationEntity)}. Each NoSQL vendor may
     * provide a more appropriate implementation.
     *
     * @param entities the entities to be saved
     * @return the saved entities
     * @throws NullPointerException when the provided collection of entities is null
     */
    Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> entities);

    /**
     * Saves multiple entities into the database with a specified time to live (TTL).
     * By default, each entity is saved individually using {@link DatabaseManager#insert(CommunicationEntity, Duration)}.
     * Each NoSQL vendor may provide a more appropriate implementation.
     *
     * @param entities the entities to be saved
     * @param ttl      the time to live for the entities
     * @return the saved entities
     * @throws NullPointerException          when either the collection of entities or the TTL is null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> entities, Duration ttl);

    /**
     * Deletes entities from the database based on the specified query.
     *
     * @param query the query used to select entities to be deleted
     * @throws NullPointerException          when either the query or the collection is null
     * @throws UnsupportedOperationException if the database does not support any operation specified in the query
     */
    void delete(DeleteQuery query);

    /**
     * Finds entities in the database based on the specified query.
     *
     * @param query the query used to select entities
     * @return a stream of entities found by the query
     * @throws NullPointerException          when the query is null
     * @throws UnsupportedOperationException if the database does not support any operation specified in the query
     */
    Stream<CommunicationEntity> select(SelectQuery query);

    /**
     * Returns the number of entities in the database that match the specified query.
     *
     * @param query the query
     * @return the number of entities returned by the query
     * @throws NullPointerException when the query is null
     */
    default long count(SelectQuery query) {
        Objects.requireNonNull(query, "query is required");
        return this.select(DefaultSelectQuery.countBy(query)).count();
    }

    /**
     * Checks if an entity matching the specified query exists in the database.
     *
     * @param query the query
     * @return true if an entity with the given query exists, false otherwise
     * @throws NullPointerException when the query is null
     */
    default boolean exists(SelectQuery query) {
        Objects.requireNonNull(query, "query is required");
        return this.select(DefaultSelectQuery.existsBy(query)).findAny().isPresent();
    }

    /**
     * Executes a query and returns the result. If the query is an insert, update, or select command,
     * it returns the result of the operation. If the query is a delete command, it returns an empty collection.
     *
     * @param query the query as a string
     * @return the result of the operation; for delete operations, an empty list is returned
     * @throws NullPointerException     when the query is null
     * @throws IllegalArgumentException when the query contains value parameters
     * @throws IllegalStateException    when there is no {@link ColumnQueryParser} available
     */
    default Stream<CommunicationEntity> query(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParser parser = new ColumnQueryParser();
        return parser.query(query, this, ColumnObserverParser.EMPTY);
    }

    /**
     * Prepares a query for execution.
     *
     * @param query the query as a string
     * @return a {@link CommunicationPreparedStatement} instance
     * @throws NullPointerException  when the query is null
     * @throws IllegalStateException when there is no {@link ColumnQueryParser} available
     */
    default CommunicationPreparedStatement prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        ColumnQueryParser parser = new ColumnQueryParser();
        return parser.prepare(query, this, ColumnObserverParser.EMPTY);
    }

    /**
     * Returns a single entity from the database based on the specified query.
     *
     * @param query the query used to select the entity
     * @return an entity wrapped in an {@link Optional}, or {@link Optional#empty()} if no entity is found
     * @throws NonUniqueResultException when more than one entity is returned by the query
     * @throws NullPointerException     when the query is null
     */
    default Optional<CommunicationEntity> singleResult(SelectQuery query) {
        Objects.requireNonNull(query, "query is required");
        Stream<CommunicationEntity> entities = select(query);
        final Iterator<CommunicationEntity> iterator = entities.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final CommunicationEntity entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("More than one entity was returned by the query: " + query);
    }

    /**
     * Returns the number of entities in the database.
     *
     * @param entity the entity name
     * @return the number of entities
     * @throws NullPointerException when the entity name is null
     * @throws UnsupportedOperationException if the database does not support this operation
     */
    long count(String entity);

    /**
     * Closes the database manager and releases any associated resources.
     */
    void close();
}
