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
package org.jnosql.diana.api.document;


import org.jnosql.diana.api.column.ColumnDeleteQuery;

import java.util.List;

/**
 * A unit that has the columnFamily and condition to delete from conditions
 *
 * @see DocumentDeleteQuery#of(String, DocumentCondition).
 * This instance will be used on:
 * <p>{@link DocumentCollectionManager#delete(DocumentDeleteQuery)}</p>
 * <p>{@link DocumentCollectionManagerAsync#delete(DocumentDeleteQuery)}</p>
 * <p>{@link DocumentCollectionManagerAsync#delete(DocumentDeleteQuery, java.util.function.Consumer)}</p>
 */
public interface DocumentDeleteQuery {

    /**
     * getter the collection name
     *
     * @return the collection name
     */
    String getCollection();

    /**
     * getter the condition
     *
     * @return the condition
     */
    DocumentCondition getCondition();

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
     * Creates a instance of column family
     *
     * @param collection the column family name
     * @param condition    the condition
     * @return an {@link ColumnDeleteQuery}
     * @throws NullPointerException when either collection
     */
    static DocumentDeleteQuery of(String collection, DocumentCondition condition) throws NullPointerException {
        return DefaultDocumentDeleteQuery.of(collection, condition);
    }
}
