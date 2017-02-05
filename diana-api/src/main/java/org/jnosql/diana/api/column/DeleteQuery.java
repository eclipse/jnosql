/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jnosql.diana.api.column;

import java.util.List;

/**
 * A unit that has the columnFamily and condition to delete from conditions
 *
 * @see DeleteQuery#of(String, ColumnCondition).
 * This instance will be used on:
 * <p>{@link ColumnFamilyManager#delete(DeleteQuery)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(DeleteQuery)}</p>
 * <p>{@link ColumnFamilyManagerAsync#delete(DeleteQuery, java.util.function.Consumer)}</p>
 */
public interface DeleteQuery {


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
    ColumnCondition getCondition();

    /**
     * Defines which columns will be removed, the database provider might use this information
     * to remove just these fields instead of all entity from {@link DeleteQuery}
     *
     * @return the columns
     */
    List<String> getColumns();

    /**
     * Adds a column to be removed
     *
     * @param column the column
     * @throws NullPointerException when column is null
     * @see DeleteQuery#getColumns()
     */
    void add(String column) throws NullPointerException;

    /**
     * Adds all columns
     *
     * @param columns the columns to be added
     * @throws NullPointerException when column is null
     * @see DeleteQuery#getColumns()
     */
    void addAll(Iterable<String> columns) throws NullPointerException;

    /**
     * Removes a column from
     *
     * @param column the column to be removed
     * @throws NullPointerException when column is null
     * @see DeleteQuery#getColumns()
     */
    void remove(String column) throws NullPointerException;

    /**
     * Removes columns
     *
     * @param columns the columns to be removed
     * @throws NullPointerException when columns is null
     * @see DeleteQuery#getColumns()
     */
    void removeAll(Iterable<String> columns) throws NullPointerException;

    /**
     * Creates a instance of column family
     *
     * @param columnFamily the column family name
     * @param condition    the condition
     * @return an {@link DeleteQuery}
     * @throws NullPointerException when either columnFamily
     */
    public static DeleteQuery of(String columnFamily, ColumnCondition condition) throws NullPointerException {
        return DefaultDeleteQuery.of(columnFamily, condition);
    }
}
