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

import java.util.*;
import java.util.stream.Collectors;

final class DefaultColumnEntity implements ColumnEntity {

    private final List<Column> columns = new ArrayList<>();

    private final String name;

    DefaultColumnEntity(String name) {
        this.name = Objects.requireNonNull(name, "name is required");
    }

    /**
     * Creates a column family instance
     *
     * @param name    a name to column family
     * @param columns - columns
     * @return a ColumnEntity instance
     */
    public static DefaultColumnEntity of(String name, Column... columns) {
        if (columns.length == 0) {
            return new DefaultColumnEntity(name);
        }
        return of(name, Arrays.asList(columns));
    }

    /**
     * Creates a column family instance
     *
     * @param name    a name to column family
     * @param columns - columns
     * @return a ColumnEntity instance
     */
    public static DefaultColumnEntity of(String name, List<Column> columns) {
        DefaultColumnEntity columnEntity = new DefaultColumnEntity(name);
        columnEntity.addAll(columns);
        return columnEntity;
    }

    public void addAll(List<Column> columns) {
        Objects.requireNonNull(columns, "The object column is required");
        this.columns.addAll(columns);
    }

    public void add(Column column) {
        Objects.requireNonNull(column, "Column is required");
        columns.add(column);
    }

    public Map<String, Object> toMap() {
        return columns.stream().collect(Collectors.toMap(Column::getName, column -> column.getValue().get()));
    }

    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean remove(String name) {
        Objects.requireNonNull(name, "columnName is required");
        return columns.removeIf(column -> column.getName().equals(name));
    }

    @Override
    public boolean remove(Column column) throws NullPointerException {
        Objects.requireNonNull(column, "column is required");
        return columns.remove(column);
    }

    @Override
    public Optional<Column> find(String name) {
        Objects.requireNonNull(name, "name is required");
        return columns.stream().filter(column -> column.getName().equals(name)).findFirst();
    }

    @Override
    public int size() {
        return columns.size();
    }

    public boolean isEmpty() {
        return columns.isEmpty();
    }

    @Override
    public ColumnEntity copy() {
        DefaultColumnEntity copy = new DefaultColumnEntity(this.name);
        copy.columns.addAll(this.columns);
        return copy;
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
        final StringBuilder sb = new StringBuilder("DefaultColumnEntity{");
        sb.append("columns=").append(columns);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
