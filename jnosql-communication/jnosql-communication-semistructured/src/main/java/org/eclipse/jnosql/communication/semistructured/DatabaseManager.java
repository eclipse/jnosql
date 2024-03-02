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
     * <p>Inserts an entity into the database. If an entity of this type with the same
     * unique identifier already exists in the database and the database supports ACID transactions,
     * then this method raises an exception. In databases that follow the BASE model
     * or use an append model to write data, this exception is not thrown.</p>
     *
     * <p>The entity instance returned as a result of this method must include all values that were
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use the instance
     * that is supplied as a parameter. This method makes no guarantees about the state of the
     * instance that is supplied as a parameter.</p>
     *
     * @param entity the entity to be saved
     * @return the saved entity
     * @throws NullPointerException when the provided entity is null
     */
    CommunicationEntity insert(CommunicationEntity entity);


    /**
     * Inserts an entity into the database with an expiration to the entity. If an entity of this type with the same
     * unique identifier already exists in the database and the database supports ACID transactions,
     * then this method raises an error. In databases that follow the BASE model
     * or use an append model to write data, this exception is not thrown.
     *
     * <p>The entity instance returned as a result of this method must include all values that were
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use the instance
     * that is supplied as a parameter. This method makes no guarantees about the state of the
     * instance that is supplied as a parameter.</p>
     *
     * <p>Time-To-Live (TTL) is a feature provided by some NoSQL databases where data is automatically removed from the
     * database after a specified duration. When inserting an entity with a TTL, the entity will be automatically deleted
     * from the database after the specified duration has passed since its insertion. If the database does not support TTL
     * or if the TTL feature is not enabled, this operation will not have any effect on the entity's expiration.</p>
     *
     * @param entity the entity to insert. Must not be {@code null}.
     * @param ttl    time to live
     * @return the inserted entity, which may or may not be a different instance depending on whether the insert caused
     * values to be generated or automatically incremented.
     * @throws NullPointerException          if the entity is null.
     * @throws UnsupportedOperationException when the database does not provide TTL
     */
    CommunicationEntity insert(CommunicationEntity entity, Duration ttl);

    /**
     * Inserts multiple entities into the database. If any entity of this type with the same
     * unique identifier as any of the given entities already exists in the database and the database
     * supports ACID transactions, then this method raises an error.
     * In databases that follow the BASE model or use an append model to write data, this exception
     * is not thrown.
     *
     * <p>The entities within the returned {@link Iterable} must include all values that were
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use
     * the entity instances that are supplied in the parameter. This method makes no guarantees
     * about the state of the entity instances that are supplied in the parameter.
     * The position of entities within the {@code Iterable} return value must correspond to the
     * position of entities in the parameter based on the unique identifier of the entity.</p>
     *
     * @param entities entities to insert.
     * @return an iterable containing the inserted entities, which may or may not be different instances depending
     * on whether the insert caused values to be generated or automatically incremented.
     * @throws NullPointerException if the iterable is null or any element is null.
     */
    Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> entities);

    /**
     * Inserts multiple entities into the database with the expiration date. If any entity of this type with the same
     * unique identifier as any of the given entities already exists in the database and the database
     * supports ACID transactions, then this method raises an error.
     * In databases that follow the BASE model or use an append model to write data, this exception
     * is not thrown.
     *
     * <p>The entities within the returned {@link Iterable} must include all values that were
     * written to the database, including all automatically generated values and incremented values
     * that changed due to the insert. After invoking this method, do not continue to use
     * the entity instances that are supplied in the parameter. This method makes no guarantees
     * about the state of the entity instances that are supplied in the parameter.
     * The position of entities within the {@code Iterable} return value must correspond to the
     * position of entities in the parameter based on the unique identifier of the entity.</p>
     *
     * <p>Time-To-Live (TTL) is a feature provided by some NoSQL databases where data is automatically removed from the
     * database after a specified duration. When inserting entities with a TTL, the entities will be automatically deleted
     * from the database after the specified duration has passed since their insertion. If the database does not support TTL
     * or if the TTL feature is not enabled, this operation will not have any effect on the expiration of the entities.</p>
     *
     * @param entities entities to insert.
     * @param ttl      time to live
     * @return an iterable containing the inserted entities, which may or may not be different instances depending
     * on whether the insert caused values to be generated or automatically incremented.
     * @throws NullPointerException if the iterable is null or any element is null.
     * @throws UnsupportedOperationException if the database does not provide time-to-live for insert operations.
     */
    Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> entities, Duration ttl);

    /**
     * Modifies an entity that already exists in the database.
     *
     * <p>For an update to be made, a matching entity with the same unique identifier
     * must be present in the database. In databases that use an append model to write data or
     * follow the BASE model, this method behaves the same as the {@link #insert} method.</p>
     *
     * <p>If the entity is versioned (for example, with an annotation or by
     * another convention from the entity model such as having an attribute named {@code version}),
     * then the version must also match. The version is automatically incremented when making
     * the update.</p>
     *
     * <p>Non-matching entities are ignored and do not cause an error to be raised.</p>
     *
     * @param entity the entity to update. Must not be {@code null}.
     * @return the updated entity, which may or may not be a different instance depending on whether the update caused
     * values to be generated or automatically incremented.
     * @throws NullPointerException if the entity is null.
     */
    CommunicationEntity update(CommunicationEntity entity);

    /**
     * Modifies entities that already exist in the database.
     *
     * <p>For an update to be made to an entity, a matching entity with the same unique identifier
     * must be present in the database. In databases that use an append model to write data or
     * follow the BASE model, this method behaves the same as the {@link #insert(Iterable)} method.</p>
     *
     * <p>If the entity is versioned (for example, with an annotation or by
     * another convention from the entity model such as having an attribute named {@code version}),
     * then the version must also match. The version is automatically incremented when making
     * the update.</p>
     *
     * <p>Non-matching entities are ignored and do not cause an error to be raised.</p>
     *
     * @param entities entities to update.
     * @return the number of matching entities that were found in the database to update.
     * @throws NullPointerException if either the iterable is null or any element is null.
     */
    Iterable<CommunicationEntity> update(Iterable<CommunicationEntity> entities);

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
        return parser.query(query, this, CommunicationObserverParser.EMPTY);
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
        return parser.prepare(query, this, CommunicationObserverParser.EMPTY);
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
