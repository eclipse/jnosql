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


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A column family is a NoSQL object that contains columns of related data. It is a tuple (pair) that consists
 * of a key-value pair, where the key is mapped to a value that is a set of columns.
 * In analogy with relational databases, a column family is as a "table", each key-value pair being a "row".
 * Each column is a tuple (triplet) consisting of a column name, a value, and a timestamp.
 * In a relational database table, this data would be grouped together within a table with other non-related data.
 *
 * @author Otávio Santana
 */
public interface ColumnFamilyEntity extends Serializable {


    /**
     * Creates a column family instance
     *
     * @param name a name to column family
     * @return a ColumnFamilyEntity instance
     */
    static ColumnFamilyEntity of(String name) {
        return new DefaultColumnFamilyEntity(name);
    }

    /**
     * Creates a column family instance
     *
     * @param name    a name to column family
     * @param columns - columns
     * @return a ColumnFamilyEntity instance
     */
    static ColumnFamilyEntity of(String name, List<Column> columns) {
        ColumnFamilyEntity columnEntity = new DefaultColumnFamilyEntity(name);
        columnEntity.addAll(columns);
        return columnEntity;
    }

    /**
     * Appends all of the columns in the column family to the end of this list.
     *
     * @param columns - columns to be added
     * @throws NullPointerException when columns is null
     */
    void addAll(List<Column> columns);

    /**
     * Appends the specified column to the end of this list
     *
     * @param column - column to be added
     * @throws NullPointerException when column is null
     */
    void add(Column column);

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link org.jnosql.diana.api.Value#get()} of the map
     *
     * @return a map instance
     */
    Map<String, Object> toMap();

    /**
     * Returns all columns from this Column Family
     *
     * @return an immutable list of columns
     */
    List<Column> getColumns();

    /**
     * Column Family's name
     *
     * @return Column Family's name
     */
    String getName();

    /**
     * Remove a column whose name is informed in parameter.
     *
     * @param columnName a column name
     * @return if a column was removed or not
     */
    boolean remove(String columnName);

    /**
     * Find document a document from name
     *
     * @param name a document name
     * @return an {@link Optional} instance with the result
     */
    Optional<Column> find(String name);

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int size();

    /**
     * Returns true if the number of columns is zero otherwise false.
     *
     * @return true if there isn't elements to {@link ColumnFamilyEntity#getColumns()}
     */
    boolean isEmpty();

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    ColumnFamilyEntity copy();

}
