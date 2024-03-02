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


import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Objects.requireNonNull;

/**
 * A unit that has the columnFamily and condition to delete from conditions.
 * This instance will be used on:
 * <p>{@link DatabaseManager#delete(DeleteQuery)}</p>
 */
public interface DeleteQuery {


    /**
     * getter the columnFamily name
     *
     * @return the columnFamily name
     */
    String name();

    /**
     * getter the condition
     * If empty, {@link Optional#empty()} is true, the implementation might either return
     * an unsupported exception or delete same elements in the database.
     *
     * @return the condition
     */
    Optional<CriteriaCondition> condition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link DeleteQuery}
     *
     * @return the columns
     */
    List<String> columns();

    /**
     * It starts the first step of {@link ColumnDelete} API using a fluent-API way.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore,
     * it might be ignored for some implementations.
     *
     * @param columns the column fields to delete query
     * @return a new {@link ColumnDelete} instance
     * @throws NullPointerException when there is a null element
     */
    static ColumnDelete delete(String... columns) {
        Stream.of(columns).forEach(d -> requireNonNull(d, "there is null column in the query"));
        return new DefaultFluentDeleteQueryBuilder(asList(columns));
    }

    /**
     * It starts the first step of {@link ColumnDelete} API using a fluent-API way.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a new {@link ColumnDelete} instance
     */
    static ColumnDelete delete() {
        return new DefaultFluentDeleteQueryBuilder(emptyList());
    }

    /**
     * It starts the first step of {@link DeleteQuery} creation using a builder pattern.
     * This first step will inform the fields to delete in the query instead of the whole record.
     * This behavior might be different for each NoSQL database provider; therefore,
     * it might be ignored for some implementations.
     *
     * @param documents the column fields to delete query
     * @return a {@link DeleteQueryBuilder} instance
     * @throws NullPointerException when there is a null element
     */
    static DeleteQueryBuilder builder(String... documents) {
        Stream.of(documents).forEach(d -> requireNonNull(d, "there is null document in the query"));
        DeleteQueryBuilder builder = new DefaultDeleteQueryBuilder();
        Stream.of(documents).forEach(builder::delete);
        return builder;
    }

    /**
     * It starts the first step of {@link DeleteQueryBuilder} creation using a builder pattern.
     * Once there is no field, it will remove the whole record instead of some fields on the database.
     *
     * @return a {@link DeleteQueryBuilder} instance
     */
    static DeleteQueryBuilder builder() {
        return new DefaultDeleteQueryBuilder();
    }


    /**
     * The initial element in the Column delete query
     */
    interface ColumnDelete {

        /**
         * Defines the column family in the delete query
         *
         * @param columnFamily the column family to query
         * @return a {@link DeleteFrom query}
         * @throws NullPointerException when columnFamily is null
         */
        DeleteFrom from(String columnFamily);
    }

    /**
     * A supplier class of {@link ColumnDelete}
     */
    interface DeleteProvider extends Function<String[], ColumnDelete>, Supplier<ColumnDelete> {
    }

    interface DeleteQueryBuilderProvider extends Function<String[], DeleteQueryBuilder>,
            Supplier<DeleteQueryBuilder> {

    }

    /**
     * The Column Delete Query
     */
    interface DeleteFrom extends DeleteQueryBuild {


        /**
         * Starts a new condition defining the  column name
         *
         * @param name the column name
         * @return a new {@link DeleteNameCondition}
         * @throws NullPointerException when name is null
         */
        DeleteNameCondition where(String name);

    }

    /**
     * The base to delete name condition
     */
    interface DeleteNameCondition {


        /**
         * Creates the equals condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DeleteWhere eq(T value);

        /**
         * Creates the like condition
         *
         * @param value the value to the condition
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        DeleteWhere like(String value);

        /**
         * Creates the greater than condition
         *
         * @param value the value to the condition
         * @param <T>   the type
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DeleteWhere gt(T value);

        /**
         * Creates the greater equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DeleteWhere gte(T value);

        /**
         * Creates the lesser than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DeleteWhere lt(T value);

        /**
         * Creates the lesser equals than condition
         *
         * @param <T>   the type
         * @param value the value to the condition
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DeleteWhere lte(T value);

        /**
         * Creates the between condition
         *
         * @param <T>    the type
         * @param valueA the values within a given range
         * @param valueB the values within a given range
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when either valueA or valueB are null
         */
        <T> DeleteWhere between(T valueA, T valueB);

        /**
         * Creates in condition
         *
         * @param values the values
         * @param <T>    the type
         * @return the {@link DeleteWhere}
         * @throws NullPointerException when value is null
         */
        <T> DeleteWhere in(Iterable<T> values);

        /**
         * Creates the equals condition
         *
         * @return {@link DeleteNotCondition}
         */
        DeleteNotCondition not();


    }

    /**
     * The column not condition
     */
    interface DeleteNotCondition extends DeleteNameCondition {
    }

    /**
     * The last step to the build of {@link DeleteQuery}.
     * It either can return a new {@link DeleteQuery} instance or execute a query with
     * {@link DatabaseManager}
     */
    interface DeleteQueryBuild {

        /**
         * Creates a new instance of {@link DeleteQuery}
         *
         * @return a new {@link DeleteQuery} instance
         */
        DeleteQuery build();

        /**
         * executes the {@link DatabaseManager#delete(DeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException when manager is null
         */
        void delete(DatabaseManager manager);

    }

    /**
     * The Column Where whose define the condition in the delete query.
     */
    interface DeleteWhere extends DeleteQueryBuild {


        /**
         * Starts a new condition in the select using {@link CriteriaCondition#and(CriteriaCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DeleteNameCondition and(String name);

        /**
         * Starts a new condition in the select using {@link CriteriaCondition#or(CriteriaCondition)}
         *
         * @param name a condition to be added
         * @return the same {@link DeleteNameCondition} with the condition appended
         * @throws NullPointerException when condition is null
         */
        DeleteNameCondition or(String name);

    }

    /**
     * Besides the fluent-API with the select {@link DeleteQuery#delete()}, the API also has support for creating
     * a {@link DeleteQuery} instance using a builder pattern.
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * <p>
     * The goal is the same; however, it provides more possibilities, such as more complex queries.
     * The ColumnQueryBuilder is not brighter than a fluent-API; it has the same validation in the creation method.
     * It is a mutable and non-thread-safe class.
     */
    interface DeleteQueryBuilder {
        /**
         * Append a new column in to delete query.
         * It informs the fields to delete in the query instead of the whole record.
         * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
         *
         * @param column a column field to delete query
         * @return the {@link DeleteQueryBuilder}
         * @throws NullPointerException when the document is null
         */
        DeleteQueryBuilder delete(String column);

        /**
         * Append a new column in to delete query.
         * This first step will inform the fields to delete in the query instead of the whole record.
         * This behavior might be different for each NoSQL database provider; therefore, it might be ignored for some implementations.
         *
         * @param columns The columns fields to delete query
         * @return the {@link DeleteQueryBuilder}
         * @throws NullPointerException when there is a null element
         */
        DeleteQueryBuilder delete(String... columns);

        /**
         * Define the column family in the query, this element is mandatory to build
         * the {@link DeleteQueryBuilder}
         *
         * @param columnFamily the column family to query
         * @return the {@link DeleteQueryBuilder}
         * @throws NullPointerException when columnFamily is null
         */
        DeleteQueryBuilder from(String columnFamily);

        /**
         * Either add or replace the condition in the query. It has a different behavior than the previous method
         * because it won't append it. Therefore, it will create when it is the first time or replace when it was executed once.
         *
         * @param condition the {@link CriteriaCondition} in the query
         * @return the {@link DeleteQueryBuilder}
         * @throws NullPointerException when condition is null
         */
        DeleteQueryBuilder where(CriteriaCondition condition);

        /**
         * It will validate and then create a {@link DeleteQuery} instance.
         *
         * @return {@link DeleteQuery}
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DeleteQueryBuilder#from(String)} method was not called.
         */
        DeleteQuery build();

        /**
         * executes the {@link DatabaseManager#delete(DeleteQuery)}
         *
         * @param manager the entity manager
         * @throws NullPointerException  when manager is null
         * @throws IllegalStateException It returns a state exception when an element is not valid or not fill-up,
         *                               such as the {@link DeleteQueryBuilder#from(String)} method was not called.
         */
        void delete(DatabaseManager manager);
    }
}
