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
