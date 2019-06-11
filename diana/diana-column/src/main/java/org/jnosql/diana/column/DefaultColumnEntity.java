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

package org.jnosql.diana.column;

import jakarta.nosql.Value;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableSet;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

/**
 * The default {@link ColumnEntity} implementation
 */
final class DefaultColumnEntity implements ColumnEntity {

    private final Map<String, Column> columns = new HashMap<>();

    private final String name;

    DefaultColumnEntity(String name) {
        this.name = Objects.requireNonNull(name, "name is required");
    }

    static DefaultColumnEntity of(String name, Column... columns) {
        if (columns.length == 0) {
            return new DefaultColumnEntity(name);
        }
        return of(name, asList(columns));
    }

    static DefaultColumnEntity of(String name, List<Column> columns) {
        DefaultColumnEntity columnEntity = new DefaultColumnEntity(name);
        columnEntity.addAll(columns);
        return columnEntity;
    }

    public void addAll(List<Column> columns) {
        Objects.requireNonNull(columns, "The object column is required");
        columns.forEach(this::add);
    }

    @Override
    public void add(Column column) {
        Objects.requireNonNull(column, "Column is required");
        this.columns.put(column.getName(), column);
    }

    @Override
    public void add(String columnName, Object value) {
        requireNonNull(columnName, "columnName is required");
        requireNonNull(value, "value is required");
        this.add(Column.of(columnName, value));
    }

    @Override
    public void add(String columnName, Value value) {
        requireNonNull(columnName, "columnName is required");
        requireNonNull(value, "value is required");
        this.add(Column.of(columnName, value));
    }

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        for (Map.Entry<String, Column> entry : columns.entrySet()) {
            Column value = entry.getValue();
            map.put(value.getName(), convert(value.get()));
        }
        return Collections.unmodifiableMap(map);
    }

    private Object convert(Object value) {
        if (value instanceof Column) {
            Column column = Column.class.cast(value);
            return Collections.singletonMap(column.getName(), convert(column.get()));
        } else if (value instanceof Iterable) {
            List<Object> list = new ArrayList<>();
            Iterable.class.cast(value).forEach(e -> list.add(convert(e)));
            return list;
        }
        return value;
    }

    @Override
    public List<Column> getColumns() {
        return columns.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .collect(collectingAndThen(toList(), Collections::unmodifiableList));
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean remove(String columnName) {
        requireNonNull(columnName, "columnName is required");
        return columns.remove(columnName) != null;
    }


    @Override
    public Optional<Column> find(String columnName) {
        requireNonNull(columnName, "columnName is required");
        Column column = columns.get(columnName);
        return ofNullable(column);
    }

    @Override
    public int size() {
        return columns.size();
    }

    @Override
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public ColumnEntity copy() {
        DefaultColumnEntity entity = new DefaultColumnEntity(this.name);
        entity.columns.putAll(new HashMap<>(this.columns));
        return entity;
    }

    @Override
    public Set<String> getColumnNames() {
        return unmodifiableSet(columns.keySet());
    }

    @Override
    public Collection<Value> getValues() {
        return columns.values().stream()
                .map(Column::getValue)
                .collect(toList());
    }

    @Override
    public boolean contains(String columnName) {
        requireNonNull(columnName, "columnName is required");
        return columns.containsKey(columnName);
    }

    @Override
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
        DefaultColumnEntity that = (DefaultColumnEntity) o;
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
}
