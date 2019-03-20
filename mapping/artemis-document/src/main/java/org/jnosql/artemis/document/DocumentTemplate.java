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
package org.jnosql.artemis.document;


import org.jnosql.artemis.Page;
import org.jnosql.artemis.PreparedStatement;
import org.jnosql.diana.api.NonUniqueResultException;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentQuery;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

/**
 * This interface that represents the common operation between an entity and DocumentCollectionEntity.
 *
 * @see org.jnosql.diana.api.document.DocumentCollectionManager
 */
public interface DocumentTemplate {

    /**
     * Saves entity
     *
     * @param entity entity to be saved
     * @param <T>    the instance type
     * @return the entity saved
     * @throws NullPointerException when document is null
     */
    <T> T insert(T entity);

    /**
     * Saves entity with time to live
     *
     * @param entity entity to be saved
     * @param <T>    the instance type
     * @param ttl    the time to live
     * @return the entity saved
     * @throws NullPointerException when either entity or ttl are null
     */
    <T> T insert(T entity, Duration ttl);

    /**
     * Saves entity, by default it's just run for each saving using
     * {@link DocumentTemplate#insert(Object)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default <T> Iterable<T> insert(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::insert).collect(toList());
    }

    /**
     * Saves documents collection entity with time to live, by default it's just run for each saving using
     * {@link DocumentTemplate#insert(Object, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default <T> Iterable<T> insert(Iterable<T> entities, Duration ttl) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(d -> insert(d, ttl)).collect(toList());
    }

    /**
     * Updates a entity
     *
     * @param entity entity to be updated
     * @param <T>    the instance type
     * @return the entity updated
     * @throws NullPointerException when entity is null
     */
    <T> T update(T entity);

    /**
     * Updates entity, by default it's just run for each saving using
     * {@link DocumentTemplate#update(Object)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param <T>      the instance type
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default <T> Iterable<T> update(Iterable<T> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::update).collect(toList());
    }

    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException query is null
     */
    void delete(DocumentDeleteQuery query);

    /**
     * Finds entities from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> List<T> select(DocumentQuery query);

    /**
     * Finds entities from query using pagination
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> Page<T> select(DocumentQueryPagination query);

    /**
     * Executes a query then bring the result as a {@link List}
     *
     * @param query the query
     * @param <T>   the entity type
     * @return the result as {@link List}
     * @throws NullPointerException when the query is null
     */
    <T> List<T> query(String query);

    /**
     * Executes a query then bring the result as a unique result
     *
     * @param query the query
     * @param <T>   the entity type
     * @return the result as {@link List}
     * @throws NullPointerException     when the query is null
     * @throws NonUniqueResultException if returns more than one result
     */
    <T> Optional<T> singleResult(String query);

    /**
     * Creates a {@link PreparedStatement} from the query
     *
     * @param query the query
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException when the query is null
     */
    PreparedStatement prepare(String query);

    /**
     * Finds by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>         the id type
     * @return the entity instance otherwise {@link Optional#empty()}
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws org.jnosql.artemis.IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> Optional<T> find(Class<T> entityClass, K id);

    /**
     * Deletes by Id.
     *
     * @param entityClass the entity class
     * @param id          the id value
     * @param <T>         the entity class type
     * @param <K>         the id type
     * @throws NullPointerException                   when either the entityClass or id are null
     * @throws org.jnosql.artemis.IdNotFoundException when the entityClass does not have the Id annotation
     */
    <T, K> void delete(Class<T> entityClass, K id);

    /**
     * Returns the number of elements from document collection
     *
     * @param documentCollection the document collection
     * @return the number of elements
     * @throws NullPointerException          when document collection is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    long count(String documentCollection);

    /**
     * Returns the number of elements from document collection
     *
     * @param <T>        entityType
     * @param entityType the document collection
     * @return the number of elements
     * @throws NullPointerException          when document collection is null
     * @throws UnsupportedOperationException when the database dot not have support
     */
    <T> long count(Class<T> entityType);

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException when the result has more than 1 entity
     * @throws NullPointerException     when query is null
     */
    default <T> Optional<T> singleResult(DocumentQuery query) {
        List<T> entities = select(query);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The query returns more than one entity, query: " + query);
    }

}
