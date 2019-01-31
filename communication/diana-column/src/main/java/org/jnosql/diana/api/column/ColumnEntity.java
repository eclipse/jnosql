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


import org.jnosql.diana.api.Value;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
    static ColumnEntity of(String name, List<Column> columns) {
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
    void addAll(List<Column> columns);

    /**
     * Appends the specified column to the end of this list
     *
     * @param column - column to be added
     * @throws NullPointerException when column is null
     */
    void add(Column column);

    /**
     * add a column within {@link ColumnEntity}
     *
     * @param name  a name of the column
     * @param value the information of the column
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String name, Object value);

    /**
     * add a column within {@link ColumnEntity}
     *
     * @param name  a name of the column
     * @param value the information of the column
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    void add(String name, Value value);

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
    boolean remove(String name);

    /**
     * Find column a column from columnName
     *
     * @param columnName a column name
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when columnName is null
     */
    Optional<Column> find(String columnName);

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

    /**
     * Returns a Set view of the names of column contained in Column Entity
     *
     * @return the keys
     */
    Set<String> getColumnNames();

    /**
     * Returns a Collection view of the values contained in this ColumnEntity.
     *
     * @return the collection of values
     */
    Collection<Value> getValues();

    /**
     * Returns true if this ColumnEntity contains a column whose the name is informed
     *
     * @param columnName the column name
     * @return true if find a column and otherwise false
     */
    boolean contains(String columnName);

    /**
     * Removes all Columns
     */
    void clear();

}
