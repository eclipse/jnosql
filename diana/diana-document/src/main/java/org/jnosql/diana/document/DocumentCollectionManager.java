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

package org.jnosql.diana.document;


import org.jnosql.diana.NonUniqueResultException;
import org.jnosql.diana.QueryException;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Interface used to interact with the persistence context to {@link DocumentEntity}
 * The DocumentCollectionManager API is used to create and remove persistent {@link DocumentEntity} instances,
 * to select entities by their primary key, and to select over entities.
 */
public interface DocumentCollectionManager extends AutoCloseable {

    /**
     * Saves document collection entity
     *
     * @param entity entity to be saved
     * @return the entity saved
     * @throws NullPointerException when document is null
     */
    DocumentEntity insert(DocumentEntity entity);


    /**
     * Saves document collection entity with time to live
     *
     * @param entity entity to be saved
     * @param ttl    the time to live
     * @return the entity saved
     * @throws NullPointerException          when either entity or ttl are null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    DocumentEntity insert(DocumentEntity entity, Duration ttl);


    /**
     * Saves documents collection entity, by default it's just run for each saving using
     * {@link DocumentCollectionManager#insert(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default Iterable<DocumentEntity> insert(Iterable<DocumentEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::insert).collect(Collectors.toList());
    }


    /**
     * Saves documents collection entity with time to live, by default it's just run for each saving using
     * {@link DocumentCollectionManager#insert(DocumentEntity, Duration)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @param ttl      time to live
     * @return the entity saved
     * @throws NullPointerException          when entities is null
     * @throws UnsupportedOperationException when the database does not support this feature
     */
    default Iterable<DocumentEntity> insert(Iterable<DocumentEntity> entities, Duration ttl) {
        Objects.requireNonNull(entities, "entities is required");
        Objects.requireNonNull(ttl, "ttl is required");
        return StreamSupport.stream(entities.spliterator(), false).map(d -> insert(d, ttl)).collect(Collectors.toList());
    }


    /**
     * Updates a entity
     *
     * @param entity entity to be updated
     * @return the entity updated
     * @throws NullPointerException when entity is null
     */
    DocumentEntity update(DocumentEntity entity);

    /**
     * Updates documents collection entity, by default it's just run for each saving using
     * {@link DocumentCollectionManager#update(DocumentEntity)},
     * each NoSQL vendor might replace to a more appropriate one.
     *
     * @param entities entities to be saved
     * @return the entity saved
     * @throws NullPointerException when entities is null
     */
    default Iterable<DocumentEntity> update(Iterable<DocumentEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false).map(this::update).collect(Collectors.toList());
    }

    /**
     * Deletes an entity
     *
     * @param query select to delete an entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    void delete(DocumentDeleteQuery query);


    /**
     * Finds {@link DocumentEntity} from select
     *
     * @param query - select to figure out entities
     * @return entities found by select
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    List<DocumentEntity> select(DocumentQuery query);


    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return the result of the operation if delete it will always return an empty list
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalArgumentException when the query has value parameters
     * @throws IllegalStateException    when there is not {@link DocumentQueryParser}
     * @throws QueryException when there is error in the syntax
     */
    default List<DocumentEntity> query(String query) {
        Objects.requireNonNull(query, "query is required");
        DocumentQueryParser parser = DocumentQueryParser.getParser();
        return parser.query(query, this, DocumentObserverParser.EMPTY);
    }

    /**
     * Executes a query and returns the result, when the operations are <b>insert</b>, <b>update</b> and <b>select</b>
     * command it will return the result of the operation when the command is <b>delete</b> it will return an empty collection.
     *
     * @param query the query as {@link String}
     * @return a {@link DocumentPreparedStatement} instance
     * @throws NullPointerException     when there is parameter null
     * @throws IllegalStateException    when there is not {@link DocumentQueryParser}
     * @throws QueryException when there is error in the syntax
     */
    default DocumentPreparedStatement  prepare(String query) {
        Objects.requireNonNull(query, "query is required");
        DocumentQueryParser parser = DocumentQueryParser.getParser();
        return parser.prepare(query, this, DocumentObserverParser.EMPTY);
    }

    /**
     * Returns a single entity from select
     *
     * @param query - select to figure out entities
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NonUniqueResultException      when the result has more than 1 entity
     * @throws NullPointerException          when select is null
     * @throws UnsupportedOperationException if the implementation does not support any operation that a query has.
     */
    default Optional<DocumentEntity> singleResult(DocumentQuery query) {
        List<DocumentEntity> entities = select(query);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.of(entities.get(0));
        }

        throw new NonUniqueResultException("The select returns more than one entity, select: " + query);
    }

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
     * closes a resource
     */
    void close();

}
