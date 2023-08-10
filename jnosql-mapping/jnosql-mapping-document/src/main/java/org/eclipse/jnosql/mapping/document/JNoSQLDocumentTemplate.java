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
package org.eclipse.jnosql.mapping.document;


import jakarta.nosql.document.DocumentTemplate;
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentQuery;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * A {@link DocumentTemplate} specialization that operates on both DocumentDeleteQuery and DocumentQuery
 */
public interface JNoSQLDocumentTemplate extends DocumentTemplate {

    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException when query is null
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
    <T> Stream<T> select(DocumentQuery query);

    /**
     * Returns the number of items in the collection that match a specified query.
     *
     * @param query the query
     * @return the number of documents from query
     * @throws NullPointerException when query is null
     */
    long count(DocumentQuery query);

    /**
     * Returns whether an entity that match a specified query.
     *
     * @param query the query
     * @return true if an entity with the given query exists, false otherwise.
     * @throws NullPointerException when query it null
     */
    boolean exists(DocumentQuery query);

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NullPointerException when query is null
     */
    <T> Optional<T> singleResult(DocumentQuery query);

    /**
     * Returns all elements from column family
     *
     * @param type the entity type
     * @param <T>  the entity type
     * @return the {@link Stream}
     * @throws NullPointerException when type is null
     */
    <T> Stream<T> findAll(Class<T> type);

    /**
     * delete elements from column family
     *
     * @param type the entity type
     * @param <T>  the entity type
     * @throws NullPointerException when type is null
     */
    <T> void deleteAll(Class<T> type);

}
