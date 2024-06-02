/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 */
package org.eclipse.jnosql.mapping.semistructured;

import jakarta.data.page.CursoredPage;
import jakarta.data.page.PageRequest;
import org.eclipse.jnosql.mapping.PreparedStatement;
import jakarta.nosql.Template;
import org.eclipse.jnosql.communication.semistructured.DeleteQuery;
import org.eclipse.jnosql.communication.semistructured.SelectQuery;

import java.util.Optional;
import java.util.stream.Stream;



/**
 * Interface representing a template for accessing a semi-structured database.
 * It extends the {@link Template} interface.
 * This interface provides methods for executing queries, counting elements, and preparing statements.
 */
public interface SemiStructuredTemplate extends Template {

    /**
     * Returns the number of elements in a specified column family.
     *
     * @param entity the name of the entity (column family)
     * @return the number of elements
     * @throws NullPointerException          if the column family name is null
     * @throws UnsupportedOperationException if the database does not support this operation
     */
    long count(String entity);

    /**
     * Returns the number of elements of a specified entity type.
     *
     * @param <T>  the entity type
     * @param type the class representing the entity type (column family)
     * @return the number of elements
     * @throws NullPointerException          if the entity type is null
     * @throws UnsupportedOperationException if the database does not support this operation
     */
    <T> long count(Class<T> type);

    /**
     * Executes a native query on the database and returns the result as a {@link Stream}.
     *
     * <p>
     * The query syntax is specific to each provider and may vary between implementations and NoSQL providers.
     * </p>
     *
     * @param query the native query
     * @param <T>   the type of the entities in the result stream
     * @return the result as a {@link Stream}
     * @throws NullPointerException          if the query is null
     * @throws UnsupportedOperationException if the provider does not support query by text
     */
    <T> Stream<T> query(String query);

    /**
     * Executes a native query on the database and returns the result as a {@link Stream}.
     *
     * <p>
     * The query syntax is specific to each provider and may vary between implementations and NoSQL providers.
     * </p>
     *
     * @param query  the native query
     * @param entity the name of the entity (column family)
     * @param <T>    the type of the entities in the result stream
     * @return the result as a {@link Stream}
     * @throws NullPointerException          if the query or entity is null
     * @throws UnsupportedOperationException if the provider does not support query by text
     */
    <T> Stream<T> query(String query, String entity);

    /**
     * Executes a query on the database and returns the result as a single unique result wrapped in an {@link Optional}.
     *
     * <p>
     * The query syntax is specific to each provider and may vary between implementations and NoSQL providers.
     * </p>
     *
     * @param query the query
     * @param <T>   the type of the entity in the result
     * @return the result as an {@link Optional}
     * @throws NullPointerException          if the query is null
     * @throws UnsupportedOperationException if the provider does not support query by text
     */
    <T> Optional<T> singleResult(String query);

    /**
     * Executes a query on the database and returns the result as a single unique result wrapped in an {@link Optional}.
     *
     * <p>
     * The query syntax is specific to each provider and may vary between implementations and NoSQL providers.
     * </p>
     *
     * @param query  the query
     * @param entity the name of the entity
     * @param <T>    the type of the entity in the result
     * @return the result as an {@link Optional}
     * @throws NullPointerException          if the query or entity is null
     * @throws UnsupportedOperationException if the provider does not support query by text
     */
    <T> Optional<T> singleResult(String query, String entity);

    /**
     * Creates a {@link PreparedStatement} from the specified query.
     *
     * <p>
     * The query syntax is specific to each provider and may vary between implementations and NoSQL providers.
     * </p>
     *
     * @param query the query
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException          if the query is null
     * @throws UnsupportedOperationException if the provider does not support query by text
     */
    PreparedStatement prepare(String query);

    /**
     * Creates a {@link PreparedStatement} from the specified query.
     *
     * <p>
     * The query syntax is specific to each provider and may vary between implementations and NoSQL providers.
     * </p>
     *
     * @param query  the query
     * @param entity the name of the entity
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException          if the query or entity is null
     * @throws UnsupportedOperationException if the provider does not support query by text
     */
    PreparedStatement prepare(String query, String entity);
    /**
     * Deletes an entity
     *
     * @param query query to delete an entity
     * @throws NullPointerException when query is null
     */
    void delete(DeleteQuery query);

    /**
     * Finds entities from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return entities found by query
     * @throws NullPointerException when query is null
     */
    <T> Stream<T> select(SelectQuery query);

    /**
     * Returns the number of items in the column family that match a specified query.
     * @param query the query
     * @return the number of documents from query
     * @throws NullPointerException when query is null
     */
    long count(SelectQuery query);

    /**
     * Returns whether an entity that match a specified query.
     * @param query the query
     * @return true if an entity with the given query exists, false otherwise.
     * @throws NullPointerException when query it null
     */
    boolean exists(SelectQuery query);

    /**
     * Returns a single entity from query
     *
     * @param query - query to figure out entities
     * @param <T>   the instance type
     * @return an entity on {@link Optional} or {@link Optional#empty()} when the result is not found.
     * @throws NullPointerException     when query is null
     */
    <T> Optional<T> singleResult(SelectQuery query);

    /**
     * Returns all entities on the database
     * @param type the entity type filter
     * @return the {@link Stream}
     * @param <T> the entity type
     * @throws NullPointerException when type is null
     */
    <T> Stream<T> findAll(Class<T> type);

    /**
     * delete all entities from the database
     * @param type the entity type filter
     * @param <T> the entity type
     * @throws NullPointerException when type is null
     */
    <T> void deleteAll(Class<T> type);

    /**
     * Select entities using pagination with cursor-based paging.
     *
     * <p>This method retrieves entities based on cursor-based paging, where the cursor acts as a bookmark for the next page of results.
     * If the provided {@link PageRequest} has a mode of {@link jakarta.data.page.PageRequest.Mode#OFFSET}, the method will consider
     * the initial request as an offset-based pagination and extract the order key to create a new {@link PageRequest} with
     * {@link jakarta.data.page.PageRequest.Mode#CURSOR_NEXT}. If the initial request is already cursor-based, the method will proceed as instructed.
     * </p>
     * <p>
     * If the cursor-based pagination is used, at least one order key is required to be specified in the {@link SelectQuery} order
     * clause; otherwise, an {@link IllegalStateException} will be thrown.
     * </p>
     *
     * @param query         the query to retrieve entities
     * @param pageRequest   the page request defining the cursor-based paging
     * @param <T>           the entity type
     * @return a {@link CursoredPage} instance containing the entities within the specified page
     * @throws NullPointerException     if the query or pageRequest is null
     * @throws IllegalStateException    if the cursor-based pagination is used without any order key specified
     */
    <T> CursoredPage<T> selectCursor(SelectQuery query, PageRequest pageRequest);
}
