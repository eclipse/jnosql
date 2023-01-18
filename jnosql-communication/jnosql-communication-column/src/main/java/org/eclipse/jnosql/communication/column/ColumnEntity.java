/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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


import org.eclipse.jnosql.communication.TypeSupplier;
import org.eclipse.jnosql.communication.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * The communication level entity. It is the API entity between the database and the Jakarta NoSQL communication level.
 * It represents a column family.
 * Each ColumnEntity has a name and one or more {@link Column}.
 *
 * @see Column
 * @see ColumnEntity#columns()
 * @see ColumnEntity#name()
 */
public class ColumnEntity {

    private final Map<String, Column> columns = new HashMap<>();

    private final String name;

    ColumnEntity(String name) {
        this.name = name;
    }


    public void addAll(List<Column> columns) {
        Objects.requireNonNull(columns, "The object column is required");
        columns.forEach(this::add);
    }

    /**
     * Appends the specified column to the end of this list
     *
     * @param column - column to be added
     * @throws NullPointerException when column is null
     */
    public void add(Column column) {
        Objects.requireNonNull(column, "Column is required");
        this.columns.put(column.name(), column);
    }

    /**
     * add a column within {@link ColumnEntity}
     *
     * @param name  a name of the column
     * @param value the information of the column
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    public void add(String name, Object value) {
        requireNonNull(name, "name is required");
        requireNonNull(value, "value is required");
        this.columns.put(name, Column.of(name, Value.of(value)));
    }

    /**
     * add a column within {@link ColumnEntity}
     *
     * @param name  a name of the column
     * @param value the information of the column
     * @throws UnsupportedOperationException when this method is not supported
     * @throws NullPointerException          when either name or value are null
     */
    public void add(String name, Value value) {
        requireNonNull(name, "name is required");
        requireNonNull(value, "value is required");
        this.columns.put(name, Column.of(name, value));
    }

    /**
     * Converts the columns to a Map where:
     * the key is the name the column
     * The value is the {@link Value#get()} of the map
     *
     * @return a map instance
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Column> entry : columns.entrySet()) {
            Column column = entry.getValue();
            map.put(entry.getKey(), convert(column.get()));
        }
        return Collections.unmodifiableMap(map);
    }

    private Object convert(Object value) {
        if (value instanceof Column) {
            Column column = Column.class.cast(value);
            return Collections.singletonMap(column.name(), convert(column.get()));
        } else if (value instanceof Iterable) {
            List<Object> list = new ArrayList<>();
            Iterable.class.cast(value).forEach(e -> list.add(convert(e)));
            return list;
        }
        return value;
    }

    /**
     * Returns all columns from this Column Family
     *
     * @return an immutable list of columns
     */
    public List<Column> columns() {
        return columns.values()
                .stream()
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Column Family's name
     *
     * @return Column Family's name
     */
    public String name() {
        return name;
    }

    /**
     * Remove a column whose name is informed in parameter.
     *
     * @param name a column name
     * @return if a column was removed or not
     * @throws NullPointerException when column is null
     */
    public boolean remove(String name) {
        requireNonNull(name, "name is required");
        return columns.remove(name) != null;
    }

    /**
     * Find column a column from columnName
     *
     * @param columnName a column name
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when columnName is null
     */
    public Optional<Column> find(String columnName) {
        requireNonNull(columnName, "columnName is required");
        Column column = columns.get(columnName);
        return ofNullable(column);
    }

    /**
     * Find a column and converts to specific value from {@link Class}
     * It is an alias to {@link Value#get(Class)}
     *
     * @param <T> the type class
     * @param columnName a name of a column
     * @param type       the type to convert the value
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when there are null parameters
     */
    public <T> Optional<T> find(String columnName, Class<T> type) {
        Objects.requireNonNull(columnName, "columnName is required");
        Objects.requireNonNull(type, "type is required");
        return ofNullable(columns.get(columnName))
                .map(c -> c.get(type));
    }

    /**
     * Find a column and converts to specific value from {@link TypeSupplier}
     * It is an alias to {@link Value#get(TypeSupplier)}
     *
     * @param <T> the type class
     * @param columnName a name of a column
     * @param type       the type to convert the value
     * @return an {@link Optional} instance with the result
     * @throws NullPointerException when there are null parameters
     */
    public <T> Optional<T> find(String columnName, TypeSupplier<T> type) {
        Objects.requireNonNull(columnName, "columnName is required");
        Objects.requireNonNull(type, "type is required");
        return ofNullable(columns.get(columnName))
                .map(v -> v.get(type));
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return columns.size();
    }

    /**
     * Returns true if the number of columns is zero otherwise false.
     *
     * @return true if there isn't elements to {@link ColumnEntity#columns()}
     */
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    /**
     * make copy of itself
     *
     * @return an instance copy
     */
    public ColumnEntity copy() {
        ColumnEntity entity = new ColumnEntity(this.name);
        entity.columns.putAll(new HashMap<>(this.columns));
        return entity;
    }

    /**
     * Returns a Set view of the names of column contained in Column Entity
     *
     * @return the keys
     */
    public Set<String> columnNames() {
        return unmodifiableSet(columns.keySet());
    }

    /**
     * Returns a Collection view of the values contained in this ColumnEntity.
     *
     * @return the collection of values
     */
    public Collection<Value> values() {
        return columns
                .values()
                .stream()
                .map(Column::value)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    /**
     * Returns true if this ColumnEntity contains a column whose the name is informed
     *
     * @param name the column name
     * @return true if find a column and otherwise false
     */
    public boolean contains(String name) {
        requireNonNull(name, "name is required");
        return columns.containsKey(name);
    }

    /**
     * Removes all Columns
     */
    public void clear() {
        columns.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnEntity that = (ColumnEntity) o;
        return Objects.equals(columns, that.columns) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, name);
    }

    @Override
    public String toString() {
        return "DefaultColumnEntity{" + "columns=" + toMap() +
                ", name='" + name + '\'' +
                '}';
    }


    /**
     * Creates a column family instance
     *
     * @param name a name to column family
     * @return a ColumnEntity instance
     */
    public static ColumnEntity of(String name) {
        return new ColumnEntity(requireNonNull(name, "name is required"));
    }

    public static ColumnEntity of(String name, List<Column> columns) {
        ColumnEntity columnEntity = new ColumnEntity(name);
        columnEntity.addAll(columns);
        return columnEntity;
    }
}
