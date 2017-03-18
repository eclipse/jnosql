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


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A column family is a NoSQL object that contains columns of related data. It is a tuple (pair) that consists
 * of a key-value pair, where the key is mapped to a value that is a set of columns.
 * In analogy with relational databases, a column family is as a "table", each key-value pair being a "row".
 * Each column is a tuple (triplet) consisting of a column name, a value, and a timestamp.
 * In a relational database table, this data would be grouped together within a table with other non-related data.
 */
public interface ColumnEntity extends Serializable {


    /**
     * Creates a column family instance
     *
     * @param name a name to column family
     * @return a ColumnEntity instance
     */
    static ColumnEntity of(String name) {
        return new DefaultColumnEntity(name);
    }

    /**
     * Creates a column family instance
     *
     * @param name    a name to column family
     * @param columns - columns
     * @return a ColumnEntity instance
     * @throws NullPointerException when either name or columns are null
     */
    static ColumnEntity of(String name, List<Column> columns) throws NullPointerException {
        Objects.requireNonNull(name, "name is required");
        Objects.requireNonNull(columns, "column is required");
        ColumnEntity columnEntity = new DefaultColumnEntity(name);
        columnEntity.addAll(columns);
        return columnEntity;
    }

    /**
     * Appends all of the columns in the column family to the end of this list.
     *
     * @param columns - columns to be added
     * @throws NullPointerException when columns is null
     */
    void addAll(List<Column> columns) throws NullPointerException;

    /**
     * Appends the specified column to the end of this list
     *
     * @param column - column to be added
     * @throws NullPointerException when column is null
     */
    void add(Column column) throws NullPointerException;

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
     * @param name a column name
     * @return if a column was removed or not
     * @throws NullPointerException when column is null
     */
    boolean remove(String name) throws NullPointerException;

    /**
     * Remove a column
     *
     * @param column a colum
     * @return if a column was removed or not
     * @throws NullPointerException when column is null
     */
    boolean remove(Column column) throws NullPointerException;

    /**
     * Find document a document from name
     *
     * @param name a document name
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when name is null
     */
    Optional<Column> find(String name) throws NullPointerException;

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    int size();

    /**
     * Returns true if the number of columns is zero otherwise false.
     *
     * @return true if there isn't elements to {@link ColumnEntity#getColumns()}
     */
    boolean isEmpty();

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    ColumnEntity copy();

}
