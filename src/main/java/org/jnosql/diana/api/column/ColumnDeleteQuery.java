/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
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
