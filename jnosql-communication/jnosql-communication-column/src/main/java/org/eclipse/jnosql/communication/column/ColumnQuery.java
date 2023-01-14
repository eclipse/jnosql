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
 * Class that contains information to do a select to {@link ColumnEntity}
 *
 * @see ColumnManager#select(ColumnQuery)
 * @see ColumnCondition
 * @see Sort
 */
public interface ColumnQuery {


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
     * The column family name
     *
     * @return the column family name
     */
    String name();

    /**
     * The conditions that contains in this {@link ColumnQuery}
     * If empty, {@link Optional#empty()} is true, the implementation might either return an unsupported exception or returns same elements in the database.
     *
     * @return the conditions
     */
    Optional<ColumnCondition> condition();

    /**
     * Returns the columns to returns in that query if empty will return all elements in the query.
     * The implementation might ignore this option.
     *
     * @return the columns
     */
    List<String> columns();

    /**
     * The sorts that contains in this {@link ColumnQuery}
     * The implementation might ignore this option.
     *
     * @return the sorts
     */
    List<Sort> sorts();

    /**
     * It starts the first step of {@link ColumnSelect} creation using a fluent-API way.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database"
     * in a database query.
     *
     * @param columns - The document fields to query, optional.
     * @return a new {@link ColumnSelect} instance
     * @throws NullPointerException                    when there is a null element
     */
    static ColumnSelect select(String... columns) {
        Stream.of(columns).forEach(d -> requireNonNull(d, "there is null document in the query"));
        return new DefaultFluentColumnQueryBuilder(Arrays.asList(columns));
    }

    /**
     * It starts the first step of {@link ColumnQuery} creation using a fluent-API way.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database" in a database query.
     * Once empty, it will return all elements in the query, similar to "select * from database" in a database query.
     *
     * @return a new {@link ColumnSelect} instance
     */
    static ColumnSelect select() {
        return new DefaultFluentColumnQueryBuilder(Collections.emptyList());
    }

    /**
     * It starts the first step of {@link ColumnQuery} creation using a builder pattern.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database" in a database query.
     *
     * @return {@link ColumnQueryBuilder} instance
     */
    static ColumnQueryBuilder builder() {
        return new DefaultColumnQueryBuilder();
    }

    /**
     * It starts the first step of {@link ColumnQuery} creation using a builder pattern.
     * This first step will inform the fields to return to the query, such as a "select field, fieldB from database" in a database query.
     * Once empty, it will return all elements in the query, similar to "select * from database" in a database query.
     *
     * @param columns The document fields to query, optional.
     * @return {@link ColumnQueryBuilder} instance
     */
    static ColumnQueryBuilder builder(String... columns) {
        Stream.of(columns).forEach(d -> requireNonNull(d, "there is null column in the query"));
        ColumnQueryBuilder builder = new DefaultColumnQueryBuilder();
        Stream.of(columns).forEach(builder::select);
        return builder;
    }


    /**
     * The ColumnFrom Query
     */
    interface ColumnFrom extends ColumnQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link ColumnNameCondition}
         * @throws NullPointerException when name is null
         */
        ColumnNameCondition where(String name);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         * The default value is zero, and it will replace the current property.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         * The default value is zero, and it will replace the current property.
         *
         * @param limit the limit
         * @return the {@link ColumnQueryBuilder}
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnOrder orderBy(String name);


    }

    /**
     * The Column Order whose define the maximum number of results to retrieve.
     */
    interface ColumnLimit extends ColumnQueryBuild {

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         * The default value is zero, and it will replace the current property.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnSkip skip(long skip);

    }

    /**
     * The Column name order a query
     */
    interface ColumnNameOrder extends ColumnQueryBuild {


        /**
         * Add the order how the result will return
         *
         * @param name the name to be ordered
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnOrder orderBy(String name);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         * The default value is zero, and it will replace the current property.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnSkip skip(long skip);


        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         * The default value is zero, and it will replace the current property.
         *
         * @param limit the limit
         * @return the {@link ColumnQueryBuilder}
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnLimit limit(long limit);


    }

    /**
     * The definition to either {@link org.eclipse.jnosql.communication.SortType}
     */
    interface ColumnOrder {


        /**
         * Defines the order as {@link org.eclipse.jnosql.communication.SortType#ASC}
         * @return the {@link ColumnNameOrder} instance
         */
        ColumnNameOrder asc();

        /**
         * Defines the order as {@link org.eclipse.jnosql.communication.SortType#DESC}
         * @return the {@link ColumnNameOrder} instance
         */
        ColumnNameOrder desc();
    }

    /**
     * The last step to the build of {@link ColumnQuery}.
     * It either can return a new {@link ColumnQuery} instance or execute a query with
     * {@link ColumnManager}
     */
    interface ColumnQueryBuild {

        /**
         * Creates a new instance of {@link ColumnQuery}
         *
         * @return a new {@link ColumnQuery} instance
         */
        ColumnQuery build();

        /**
         * Executes {@link ColumnManager#select(ColumnQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link ColumnManager#select(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        Stream<ColumnEntity> getResult(ColumnManager manager);

        /**
         * Executes {@link ColumnManager#singleResult(ColumnQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link ColumnManager#singleResult(ColumnQuery)}
         * @throws NullPointerException when manager is null
         */
        Optional<ColumnEntity> getSingleResult(ColumnManager manager);

    }

    /**
     * The initial element in the Column query
     */
    interface ColumnSelect {

        /**
         * Defines the column family in the query
         *
         * @param columnFamily the column family to query
         * @return a {@link ColumnFrom query}
         * @throws NullPointerException when columnFamily is null
         */
        ColumnFrom from(String columnFamily);
    }

    /**
     * A provider class of {@link ColumnSelect}
     */
    interface ColumnSelectProvider extends Function<String[], ColumnSelect>, Supplier<ColumnSelect> {
    }

    /**
     * A provider class of {@link ColumnQueryBuilder}
     */
    interface ColumnQueryBuilderProvider extends Function<String[], ColumnQueryBuilder>, Supplier<ColumnQueryBuilder> {
    }


    /**
     * The Column Order whose define the position of the first result to retrieve.
     */
    interface ColumnSkip extends ColumnQueryBuild {


        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         * The default value is zero, and it will replace the current property.
         *
         * @param limit the limit
         * @return the {@link ColumnQueryBuilder}
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnLimit limit(long limit);

    }

    /**
     * The Column Where whose define the condition in the query.
     */
    interface ColumnWhere extends ColumnQueryBuild {


        /**
         * Starts a new condition in the select using
         * {@link ColumnCondition#and(ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnNameCondition and(String name);

        /**
         * Appends a new condition in the select using
         * {@link ColumnCondition#or(ColumnCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link ColumnNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        ColumnNameCondition or(String name);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         * The default value is zero, and it will replace the current property.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnSkip skip(long skip);

        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         * The default value is zero, and it will replace the current property.
         *
         * @param limit the limit
         * @return the {@link ColumnQueryBuilder}
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnLimit limit(long limit);

        /**
         * Add the order how the result will return
         *
         * @param name the name to order
         * @return a query with the sort defined
         * @throws NullPointerException when name is null
         */
        ColumnOrder orderBy(String name);

    }

    /**
     * The base to name condition
     */
    interface ColumnNameCondition {


        /**
         * Creates the equals condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere eq(T value);

        /**
         * Creates the like condition
         *
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        ColumnWhere like(String value);

        /**
         * Creates the greater than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere gt(T value);

        /**
         * Creates the greater equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere gte(T value);

        /**
         * Creates the lesser than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere lt(T value);

        /**
         * Creates the lesser equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere lte(T value);

        /**
         * Creates the between condition
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> ColumnWhere between(T valueA, T valueB);

        /**
         * Creates in condition
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link ColumnWhere}
         * @throws NullPointerException when value is null
         */
        <T> ColumnWhere in(Iterable<T> values);

        /**
         * Creates the equals condition
         *
         * @return {@link ColumnNotCondition}
         */
        ColumnNotCondition not();

    }

    /**
     * The column not condition
     */
    interface ColumnNotCondition extends ColumnNameCondition {
    }

    /**
     * Besides, the fluent-API with the select method, the API also has support for creating a {@link ColumnQuery} instance using a builder pattern.
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * The ColumnQueryBuilder is not brighter than a fluent-API; it has the same validation in the creation method.
     * It is a mutable and non-thread-safe class.
     */
    interface ColumnQueryBuilder {
        /**
         * Append a new column in the search result. The query will return the result by elements declared such as "select column from database"
         * If it remains empty, it will return all the possible fields, similar to "select * from database"
         *
         * @param column a field to return to the search
         * @return the {@link ColumnQueryBuilder}
         * @throws NullPointerException when the document is null
         */
        ColumnQueryBuilder select(String column);

        /**
         * Append new columns in the search result. The query will return the result by elements declared such as "select column from database"
         * If it remains empty, it will return all the possible fields, similar to "select * from database"
         *
         * @param columns a field to return to the search
         * @return the {@link ColumnQueryBuilder}
         * @throws NullPointerException when there is a null element
         */
        ColumnQueryBuilder select(String... columns);

        /**
         * Append a new sort in the query. The first one has more precedence than the next one.
         *
         * @param sort the {@link Sort}
         * @return the {@link ColumnQueryBuilder}
         * @throws NullPointerException when the sort is null
         */
        ColumnQueryBuilder sort(Sort sort);

        /**
         * Append sorts in the query. The first one has more precedence than the next one.
         *
         * @param sorts the array of {@link Sort}
         * @return the {@link ColumnQueryBuilder}
         * @throws NullPointerException when there is a null sort
         */
        ColumnQueryBuilder sort(Sort... sorts);

        /**
         * Define the column family in the query, this element is mandatory to build the {@link ColumnQuery}
         *
         * @param columnFamily the column family to query
         * @return the {@link ColumnQueryBuilder}
         * @throws NullPointerException when documentCollection is null
         */
        ColumnQueryBuilder from(String columnFamily);

        /**
         * Either add or replace the condition in the query. It has a different behavior than the previous method
         * because it won't append it. Therefore, it will create when it is the first time or replace when it was executed once.
         *
         * @param condition the {@link ColumnCondition} in the query
         * @return the {@link ColumnQueryBuilder}
         * @throws NullPointerException when condition is null
         */
        ColumnQueryBuilder where(ColumnCondition condition);

        /**
         * Defines the position of the first result to retrieve.
         * It will depend on the NoSQL vendor implementation, but it will discard or skip the search result.
         * The default value is zero, and it will replace the current property.
         *
         * @param skip the first result to retrieve
         * @return a query with first result defined
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnQueryBuilder skip(long skip);

        /**
         * Defines the maximum number of results to retrieve.
         * It will truncate to be no longer than limit.
         * The default value is zero, and it will replace the current property.
         *
         * @param limit the limit
         * @return the {@link ColumnQueryBuilder}
         * @throws IllegalArgumentException if limit is negative
         */
        ColumnQueryBuilder limit(long limit);

        /**
         * It will validate and then create a {@link ColumnQuery} instance.
         *
         * @return {@link ColumnQuery}
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link ColumnQueryBuilder#from(String)} method was not called.
         */
        ColumnQuery build();

        /**
         * Executes {@link ColumnManager#select(ColumnQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link ColumnManager#select(ColumnQuery)}
         * @throws NullPointerException  when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link ColumnQueryBuilder#from(String)} method was not called.
         */
        Stream<ColumnEntity> getResult(ColumnManager manager);

        /**
         * Executes {@link ColumnManager#singleResult(ColumnQuery)}
         *
         * @param manager the entity manager
         * @return the result of {@link ColumnManager#singleResult(ColumnQuery)}
         * @throws NullPointerException  when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link ColumnQueryBuilder#from(String)} method was not called.
         */
        Optional<ColumnEntity> getSingleResult(ColumnManager manager);
    }
}
