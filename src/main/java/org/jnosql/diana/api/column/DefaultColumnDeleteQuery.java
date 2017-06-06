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


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Objects.requireNonNull;

/**
 * the default implementation of {@link ColumnDeleteQuery}
 */
class DefaultColumnDeleteQuery implements ColumnDeleteQuery {

    private final String columnFamily;

    private ColumnCondition condition;

    private final List<String> columns = new ArrayList<>();

    private DefaultColumnDeleteQuery(String columnFamily, ColumnCondition condition) {
        this.columnFamily = columnFamily;
        this.condition = condition;
    }

    private DefaultColumnDeleteQuery(String columnFamily) {
        this.columnFamily = columnFamily;
    }

    public String getColumnFamily() {
        return columnFamily;
    }

    public Optional<ColumnCondition> getCondition() {
        return Optional.ofNullable(condition);
    }

    @Override
    public List<String> getColumns() {
        return unmodifiableList(columns);
    }

    @Override
    public void add(String column) throws NullPointerException {
        this.columns.add(requireNonNull(column, "column null is required"));
    }

    @Override
    public void addAll(Iterable<String> columns) throws NullPointerException {
        requireNonNull(columns, "columns is required");
        columns.forEach(this::add);
    }

    @Override
    public void remove(String column) throws NullPointerException {
        requireNonNull(column, "column is required");
        this.columns.remove(column);
    }

    @Override
    public void removeAll(Iterable<String> columns) throws NullPointerException {
        requireNonNull(columns, "columns is required");
        columns.forEach(this::remove);
    }

    @Override
    public ColumnDeleteQuery and(ColumnCondition condition) {
        requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.and(condition);
        }
        return this;
    }

    @Override
    public ColumnDeleteQuery with(ColumnCondition condition) throws NullPointerException {
        Objects.requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public ColumnDeleteQuery or(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        if (Objects.isNull(this.condition)) {
            this.condition = condition;
        } else {
            this.condition = this.condition.or(condition);
        }
        return this;
    }

    static DefaultColumnDeleteQuery of(String columnFamily, ColumnCondition condition) throws NullPointerException {
        requireNonNull(columnFamily, "columnFamily is required");
        requireNonNull(condition, "condition is required");
        return new DefaultColumnDeleteQuery(columnFamily, condition);
    }

    static DefaultColumnDeleteQuery of(String columnFamily) throws NullPointerException {
        requireNonNull(columnFamily, "columnFamily is required");
        return new DefaultColumnDeleteQuery(columnFamily);
    }
}
