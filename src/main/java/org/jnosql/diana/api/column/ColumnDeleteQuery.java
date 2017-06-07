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
package org.jnosql.diana.api.column;

import java.util.List;
import java.util.Optional;

/**
 * A unit that has the columnFamily and condition to delete from conditions.
 * <p>{@link  ColumnDeleteQuery#of(String, ColumnCondition)}</p>
 * This instance will be used on:
 * <p>{@link ColumnFamilyManager#delete(ColumnDeleteQuery)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(ColumnDeleteQuery)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(ColumnDeleteQuery, java.util.function.Consumer)}</p>
 */
public interface ColumnDeleteQuery {


    /**
     * getter the columnFamily name
     *
     * @return the columnFamily name
     */
    String getColumnFamily();

    /**
     * getter the condition
     *
     * @return the condition
     */
    Optional<ColumnCondition> getCondition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link ColumnDeleteQuery}
     *
     * @return the columns
     */
    List<String> getColumns();

    /**
     * Adds a column to be removed
     *
     * @param column the column
     * @throws NullPointerException when column is null
     * @see ColumnDeleteQuery#getColumns()
     */
    void add(String column) throws NullPointerException;

    /**
     * Adds all columns
     *
     * @param columns the columns to be added
     * @throws NullPointerException when column is null
     * @see ColumnDeleteQuery#getColumns()
     */
    void addAll(Iterable<String> columns) throws NullPointerException;

    /**
     * Removes a column from
     *
     * @param column the column to be removed
     * @throws NullPointerException when column is null
     * @see ColumnDeleteQuery#getColumns()
     */
    void remove(String column) throws NullPointerException;

    /**
     * Removes columns
     *
     * @param columns the columns to be removed
     * @throws NullPointerException when columns is null
     * @see ColumnDeleteQuery#getColumns()
     */
    void removeAll(Iterable<String> columns) throws NullPointerException;


    /**
     * Appends a new condition in the select using {@link ColumnCondition#and(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    ColumnDeleteQuery and(ColumnCondition condition);

    /**
     * Sets the column condition
     *
     * @param condition
     * @return this instance
     * @throws NullPointerException when condition is null
     */
    ColumnDeleteQuery with(ColumnCondition condition) throws NullPointerException;


    /**
     * Appends a new condition in the select using {@link ColumnCondition#or(ColumnCondition)}
     *
     * @param condition a condition to be added
     * @return the same instance with a condition added
     * @throws NullPointerException when condition is null
     */
    ColumnDeleteQuery or(ColumnCondition condition) throws NullPointerException;

    /**
     * Creates a instance of column family
     *
     * @param columnFamily the column family name
     * @param condition    the condition
     * @return an {@link ColumnDeleteQuery}
     * @throws NullPointerException when either columnFamily
     */
    static ColumnDeleteQuery of(String columnFamily, ColumnCondition condition) throws NullPointerException {
        return DefaultColumnDeleteQuery.of(columnFamily, condition);
    }

    /**
     * Creates a instance of column family
     *
     * @param columnFamily the column family name
     * @return an {@link ColumnDeleteQuery}
     * @throws NullPointerException when either columnFamily
     */
    static ColumnDeleteQuery of(String columnFamily) throws NullPointerException {
        return DefaultColumnDeleteQuery.of(columnFamily);
    }
}
