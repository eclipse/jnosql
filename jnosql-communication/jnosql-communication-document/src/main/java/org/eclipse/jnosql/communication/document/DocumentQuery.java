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



import jakarta.data.repository.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Class that contains information to do a select to {@link DocumentManager}
 *
 * @see DocumentManager#select(DocumentQuery)
 * @see DocumentCondition
 * @see Sort
 */
public interface DocumentQuery {


    /**
     * @return The maximum number of results the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long limit();

    /**
     * @return The position of the first result the select object was set to retrieve.
     * The implementation might ignore this option.
     */
    long skip();


    /**
     * The document collection name
     *
     * @return the document collection name
     */
    String name();

    /**
     * The conditions that contains in this {@link DocumentQuery}
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or returns same elements in the database.
     *
     * @return the conditions
     */
    Optional<DocumentCondition> condition();

    /**
     * The sorts that contains in this {@link DocumentQuery}
     * The implementation might ignore this option.
     *
     * @return the sorts
     */
    List<Sort> sorts();

    /**
     * Returns the documents to returns in that query if empty will return all elements in the query.
     * The implementation might ignore this option.
     *
     * @return the documents
     */
    List<String> documents();

    /**
     * It starts the first step of {@link DocumentQuery} creation using a fluent-API way.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database"
     * in a database query.
     *
     * @param documents - The document fields to query, optional.
     * @return a new {@link DocumentSelect} instance
     * @throws NullPointerException                    when there is a null element
     */
    static DocumentSelect select(String... documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        return new DefaultFluentDocumentQueryBuilder(Arrays.asList(documents));
    }

    /**
     * It starts the first step of {@link DocumentQuery} creation using a fluent-API way.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database" in a database query.
     * Once empty, it will return all elements in the query, similar to "select * from database" in a database query.
     *
     * @return a new {@link DocumentSelect} instance
     */
    static DocumentSelect select() {
        return new DefaultFluentDocumentQueryBuilder(Collections.emptyList());
    }

    /**
     * It starts the first step of {@link DocumentQuery} creation using a builder pattern.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database" in a database query.
     *
     * @return {@link DocumentQueryBuilder} instance
     */
    static DocumentQueryBuilder builder() {
        return new DefaultDocumentQueryBuilder();
    }

    /**
     * It starts the first step of {@link DocumentQuery} creation using a builder pattern.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database" in a database query.
     * Once empty, it will return all elements in the query, similar to "select * from database" in a database query.
     *
     * @param documents The document fields to query, optional.
     * @return {@link DocumentQueryBuilder} instance
     */
    static DocumentQueryBuilder builder(String... documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        DefaultDocumentQueryBuilder builder = new DefaultDocumentQueryBuilder();
        Stream.of(documents).forEach(builder::select);
        return builder;
    }

    /**
     * The DocumentFrom Query
     */
    interface DocumentFrom extends DocumentQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DocumentNameCondition}
         * @throws NullPointerException when name is null
         */
        DocumentNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if skip is negative
         */
        DocumentSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         *
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException if limit is negative
         */
        DocumentLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentOrder orderBy(String name);


    }

    /**
     * A provider class of {@link DocumentSelect}
     */
    interface DocumentSelectProvider extends Function<String[], DocumentSelect>, Supplier<DocumentSelect> {
    }

    /**
     * A provider class of {@link DocumentQueryBuilder}
     */
    interface DocumentQueryBuilderProvider extends Function<String[], DocumentQueryBuilder>, Supplier<DocumentQueryBuilder> {

    }


    /**
     * The Document Order whose define the maximum number of results to retrieve.
     */
    interface DocumentLimit extends DocumentQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if skip is negative
         */
        DocumentSkip skip(long skip);

    }

    /**
     * The base to name condition
     */
    interface DocumentNameCondition {


        /**
         * Creates the equals condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere eq(T value);

        /**
         * Creates the like condition
         *
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        DocumentWhere like(String value);

        /**
         * Creates the greater than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere gt(T value);

        /**
         * Creates the greater equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere gte(T value);

        /**
         * Creates the lesser than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere lt(T value);

        /**
         * Creates the lesser equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere lte(T value);

        /**
         * Creates the between condition
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DocumentWhere between(T valueA, T valueB);

        /**
         * Creates in condition
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DocumentWhere}
         * @throws NullPointerException when value is null
         */
        <T> DocumentWhere in(Iterable<T> values);

        /**
         * Creates the equals condition
         *
         * @return {@link DocumentNotCondition}
         */
        DocumentNotCondition not();
    }

    /**
     * The Column name order to the builder
     */
    interface DocumentNameOrder extends DocumentQueryBuild {

        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentOrder orderBy(String name);


        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if skip is negative
         */
        DocumentSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         *
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException if limit is negative
         */
        DocumentLimit limit(long limit);


    }

    /**
     * The column not condition
     */
    interface DocumentNotCondition extends DocumentNameCondition {
    }

    /**
     * The initial element in the Document query
     */
    interface DocumentSelect {

        /**
         * Defines the document collection in the query
         *
         * @param documentCollection the document collection to query
         * @return a {@link DocumentFrom query}
         * @throws NullPointerException when documentCollection is null
         */
        DocumentFrom from(String documentCollection);
    }

    /**
     * The Document Order whose define the position of the first result to retrieve.
     */
    interface DocumentSkip extends DocumentQueryBuild {

        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         *
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException if limit is negative
         */
        DocumentLimit limit(long limit);

    }

    /**
     * The Document Where whose define the condition in the query.
     */
    interface DocumentWhere extends DocumentQueryBuild {


        /**
         * Starts a new condition in the select using {@link DocumentCondition#and(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentNameCondition and(String name);

        /**
         * Appends a new condition in the select using {@link DocumentCondition#or(DocumentCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DocumentNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DocumentNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if skip is negative
         */
        DocumentSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         *
         * @param limit the limit
         * @return a query with the limit defined
         * @throws IllegalArgumentException if limit is negative
         */
        DocumentLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        DocumentOrder orderBy(String name);

    }

    /**
     * The Document Order whose define the sort in the query.
     */
    interface DocumentOrder {

        /**
         * Defines the order as {@link jakarta.data.repository.Direction#ASC}
         *
         * @return the {@link DocumentNameOrder} instance
         */
        DocumentNameOrder asc();

        /**
         * Defines the order as {@link jakarta.data.repository.Direction#DESC}
         *
         * @return the {@link DocumentNameOrder} instance
         */
        DocumentNameOrder desc();
    }

    /**
     * The last step to the build of {@link DocumentQuery}.
     * It either can return a new {@link DocumentQuery} instance or execute a query with
     * {@link DocumentManager}
     */
    interface DocumentQueryBuild {

        /**
         * Creates a new instance of {@link DocumentQuery}
         *
         * @return a new {@link DocumentQuery} instance
         */
        DocumentQuery build();

        /**
         * Executes {@link DocumentManager#select(DocumentQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link DocumentManager#select(DocumentQuery)}
         * @throws NullPointerException when manager is null
         */
        Stream<DocumentEntity> getResult(DocumentManager manager);

        /**
         * Executes {@link DocumentManager#singleResult(DocumentQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link DocumentManager#singleResult(DocumentQuery)}
         * @throws NullPointerException when manager is null
         */
        Optional<DocumentEntity> getSingleResult(DocumentManager manager);

    }

    /**
     * Besides, the fluent-API with the select method, the API also has support for creating a {@link DocumentQuery} instance using a builder pattern.
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * The DocumentQueryBuilder is not brighter than a fluent-API; it has the same validation in the creation method.
     * It is a mutable and non-thread-safe class.
     */
    interface DocumentQueryBuilder {
        /**
         * Append a new document in the search result. The query will return the result by elements declared such as "select column from database"
         * If it remains empty, it will return all the possible fields, similar to "select * from database"
         *
         * @param document a field to return to the search
         * @return the {@link DocumentQueryBuilder}
         * @throws NullPointerException when the document is null
         */
        DocumentQueryBuilder select(String document);

        /**
         * Append new documents in the search result. The query will return the result by elements declared such as "select column from database"
         * If it remains empty, it will return all the possible fields, similar to "select * from database"
         *
         * @param documents a field to return to the search
         * @return the {@link DocumentQueryBuilder}
         * @throws NullPointerException when there is a null element
         */
        DocumentQueryBuilder select(String... documents);

        /**
         * Append a new sort in the query. The first one has more precedence than the next one.
         *
         * @param sort the {@link Sort}
         * @return the {@link DocumentQueryBuilder}
         * @throws NullPointerException when the sort is null
         */
        DocumentQueryBuilder sort(Sort sort);

        /**
         * Append sorts in the query. The first one has more precedence than the next one.
         *
         * @param sorts the array of {@link Sort}
         * @return the {@link DocumentQueryBuilder}
         * @throws NullPointerException when there is a null sort
         */
        DocumentQueryBuilder sort(Sort... sorts);

        /**
         * Define the document collection in the query, this element is mandatory to build the {@link DocumentQuery}
         *
         * @param documentCollection the document collection to query
         * @return the {@link DocumentQueryBuilder}
         * @throws NullPointerException when documentCollection is null
         */
        DocumentQueryBuilder from(String documentCollection);

        /**
         * Either add or replace the condition in the query. It has a different behavior than the previous method
         * because it won't append it. Therefore, it will create when it is the first time or replace when it was executed once.
         *
         * @param condition the {@link DocumentCondition} in the query
         * @return the {@link DocumentQueryBuilder}
         * @throws NullPointerException when condition is null
         */
        DocumentQueryBuilder where(DocumentCondition condition);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         * The default value is zero, and it will replace the current property.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if limit is negative
         */
        DocumentQueryBuilder skip(long skip);

        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         * The default value is zero, and it will replace the current property.
         *
         * @param limit the limit
         * @return the {@link DocumentQueryBuilder}
         * @throws IllegalArgumentException if limit is negative
         */
        DocumentQueryBuilder limit(long limit);

        /**
         * It will validate and then create a {@link DocumentQuery} instance.
         *
         * @return {@link DocumentQuery}
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DocumentQueryBuilder#from(String)} method was not called.
         */
        DocumentQuery build();

        /**
         * Executes {@link DocumentManager#select(DocumentQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link DocumentManager#select(DocumentQuery)}
         * @throws NullPointerException  when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DocumentQueryBuilder#from(String)} method was not called.
         */
        Stream<DocumentEntity> getResult(DocumentManager manager);

        /**
         * Executes {@link DocumentManager#singleResult(DocumentQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link DocumentManager#singleResult(DocumentQuery)}
         * @throws NullPointerException  when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DocumentQueryBuilder#from(String)} method was not called.
         */
        Optional<DocumentEntity> getSingleResult(DocumentManager manager);
    }
}
