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
package org.eclipse.jnosql.communication.document;



import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * A unit that has the columnFamily and condition to delete from conditions
 * <p>
 * This instance will be used on:
 * <p>{@link DocumentManager#delete(DocumentDeleteQuery)}</p>
 */
public interface DocumentDeleteQuery {

    /**
     * getter the collection name
     *
     * @return the collection name
     */
    String documentCollection();

    /**
     * getter the condition
     *
     * @return the condition
     */
    Optional<DocumentCondition> condition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link DocumentDeleteQuery}
     *
     * @return the columns
     */
    List<String> documents();

    /**
     * It starts the first step of {@link DocumentDelete} API using a fluent-API way.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
     *
     * @param documents the column fields to delete query
     * @return a new {@link DocumentDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static DocumentDelete delete(String... documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        return new DefaultFluentDeleteQueryBuilder(Arrays.asList(documents));
    }

    /**
     * It starts the first step of {@link DocumentDelete} API using a fluent-API way.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a new {@link DocumentDelete} instance
     */
    static DocumentDelete delete() {
        return new DefaultFluentDeleteQueryBuilder(Collections.emptyList());
    }

    /**
     * It starts the first step of {@link DocumentDeleteQuery} creation using a builder pattern.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
     *
     * @param documents the column fields to delete query
     * @return a {@link DocumentDeleteQueryBuilder} instance
     * @throws NullPointerException when there is a null element
     */
    static DocumentDeleteQueryBuilder builder(String... documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        DefaultDocumentDeleteQueryBuilder builder = new DefaultDocumentDeleteQueryBuilder();
        Stream.of(documents).forEach(builder::delete);
        return builder;
    }

    /**
     * It starts the first step of {@link DocumentDeleteQuery} creation using a builder pattern.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a {@link DocumentDeleteQueryBuilder} instance
     */
    static DocumentDeleteQueryBuilder builder() {
        return new DefaultDocumentDeleteQueryBuilder();
    }


    /**
     * The initial element in the Document delete query
     */
    interface DocumentDelete {

        /**
         * Defines the document collection in the delete query
         *
         * @param documentCollection the document collection to query
         * @return a {@link DocumentDeleteFrom query}
         * @throws NullPointerException when documentCollection is null
         */
        DocumentDeleteFrom from(String documentCollection);

    }

    /**
     * A provider class of {@link DocumentDelete}
     */
    interface DocumentDeleteProvider extends Function<String[], DocumentDelete>, Supplier<DocumentDelete> {
    }



    /**
     * The Document Delete Query
     */
    interface DocumentDeleteFrom extends DocumentDeleteQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DocumentDeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        DocumentDeleteNameCondition where(String name);

    }

    /**
     * The base to delete name condition
     */
    interface DocumentDeleteNameCondition {

        /**
         * Creates the equals condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere eq(T value);

        /**
         * Creates the like condition
         *
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        DocumentDeleteWhere like(String value);

        /**
         * Creates the greater than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere gte(T value);

        /**
         * Creates the lesser than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere lte(T value);

        /**
         * Creates the between condition
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DocumentDeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DocumentDeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentDeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition
         *
         * @return {@link DocumentDeleteNotCondition}
         */
        DocumentDeleteNotCondition not();

    }

    /**
     * The document not condition
     */
    interface DocumentDeleteNotCondition extends DocumentDeleteNameCondition {
    }

    /**
     * The last step to the build of {@link DocumentDeleteQuery}.
     * It either can return a new {@link DocumentDeleteQuery} instance or execute a query with
     * {@link DocumentManager}
     */
    interface DocumentDeleteQueryBuild {

        /**
         * Creates a new instance of {@link DocumentDeleteQuery}
         *
         * @return a new {@link DocumentDeleteQuery} instance
         */
        DocumentDeleteQuery build();

        /**
         * executes the {@link DocumentManager#delete(DocumentDeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException when manager is null
         */
        void delete(DocumentManager manager);

    }

    /**
     * The Document Where whose define the condition in the delete query.
     */
    interface DocumentDeleteWhere extends DocumentDeleteQueryBuild {


        /**
         * Starts a new condition in the select using {@link DocumentCondition#and(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentDeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link DocumentCondition#or(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentDeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentDeleteNameCondition or(String name);

    }

    /**
     * Besides the fluent-API with the select {@link DocumentDeleteQuery#delete()}, the API also has support for creating
     * a {@link DocumentDeleteQuery} instance using a builder pattern.
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * <p>
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * The DocumentQueryBuilder is not brighter than a fluent-API; it has the same validation in the creation method.
     * It is a mutable and non-thread-safe class.
     */
    interface DocumentDeleteQueryBuilder {
        /**
         * Append a new document in to delete query.
         * It informs the fields to delete in the query instead of the whole record.
         * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
         *
         * @param document a column field to delete query
         * @return the {@link DocumentDeleteQueryBuilder}
         * @throws NullPointerException when the document is null
         */
        DocumentDeleteQueryBuilder delete(String document);

        /**
         * Append a new document in to delete query.
         * This first step will inform the fields to delete in the query instead of the whole record.
         * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
         *
         * @param documents The column fields to delete query
         * @return the {@link DocumentDeleteQueryBuilder}
         * @throws NullPointerException when there is a null element
         */
        DocumentDeleteQueryBuilder delete(String... documents);

        /**
         * Define the document collection in the query, this element
         * is mandatory to build the {@link DocumentDeleteQuery}
         *
         * @param documentCollection the document collection to query
         * @return the {@link DocumentDeleteQueryBuilder}
         * @throws NullPointerException when documentCollection is null
         */
        DocumentDeleteQueryBuilder from(String documentCollection);

        /**
         * Either add or replace the condition in the query. It has a different behavior than the previous method
         * because it won't append it. Therefore, it will create when it is the first time or replace when it was executed once.
         *
         * @param condition the {@link DocumentCondition} in the query
         * @return the {@link DocumentDeleteQueryBuilder}
         * @throws NullPointerException when condition is null
         */
        DocumentDeleteQueryBuilder where(DocumentCondition condition);

        /**
         * It will validate and then create a {@link DocumentDeleteQuery} instance.
         *
         * @return {@link DocumentDeleteQuery}
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DocumentDeleteQueryBuilder#from(String)} method was not called.
         */
        DocumentDeleteQuery build();

        /**
         * executes the {@link DocumentManager#delete(DocumentDeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DocumentDeleteQueryBuilder#from(String)} method was not called.
         */
        void delete(DocumentManager manager);
    }
}
