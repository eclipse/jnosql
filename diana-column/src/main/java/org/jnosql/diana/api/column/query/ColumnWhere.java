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
package org.jnosql.diana.api.column.query;


import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnQuery;

/**
 * The Column Where whose define the condition in the query.
 */
public interface ColumnWhere {


    /**
     * Appends a new condition in the select using {@link ColumnCondition#and(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same {@link ColumnWhere} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnWhere and(ColumnCondition condition) throws NullPointerException;

    /**
     * Starts a new condition in the select using {@link ColumnCondition#or(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same {@link ColumnWhere} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnWhere or(ColumnCondition condition) throws NullPointerException;

    /**
     * Starts a new condition in the select using {@link ColumnCondition#and(ColumnCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link ColumnNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnNameCondition and(String name) throws NullPointerException;

    /**
     * Appends a new condition in the select using {@link ColumnCondition#or(ColumnCondition)}
     *
     * @param name a condition to be added
     * @return the same {@link ColumnNameCondition} with the condition appended
     * @throws NullPointerException when condition is null
     */
    ColumnNameCondition or(String name) throws NullPointerException;

    /**
     * Defines the position of the first result to retrieve.
     *
     * @param start the first result to retrive
     * @return a query with first result defined
     */
    ColumnStart start(long start);


    /**
     * Defines the maximum number of results to retrieve.
     *
     * @param limit the limit
     * @return a query with the limit defined
     */
    ColumnLimit limit(long limit);

    /**
     * Add the order how the result will returned
     *
     * @param name the name to order
     * @return a query with the sort defined
     * @throws NullPointerException when name is null
     */
    ColumnNameOrder orderBy(String name) throws NullPointerException;

    /**
     * Creates a new instance of {@link ColumnQuery}
     *
     * @return a new {@link ColumnQuery} instance
     */
    ColumnQuery build();

}
