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
package org.eclipse.jnosql.diana.column.query;


import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import org.eclipse.jnosql.diana.column.DefaultColumnCondition;
import org.eclipse.jnosql.diana.column.DefaultColumnConditionProvider;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * The default implementation of {@link ColumnDeleteQuery}
 */
class DefaultColumnDeleteQuery implements ColumnDeleteQuery {

    private final String columnFamily;

    private final ColumnCondition condition;

    private final List<String> columns;

    DefaultColumnDeleteQuery(String columnFamily, ColumnCondition condition, List<String> columns) {
        this.columnFamily = columnFamily;
        this.condition = ofNullable(condition).map(DefaultColumnCondition::readOnly).orElse(null);
        this.columns = columns;
    }

    @Override
    public String getColumnFamily() {
        return columnFamily;
    }

    @Override
    public Optional<ColumnCondition> getCondition() {
        return ofNullable(condition);
    }

    @Override
    public List<String> getColumns() {
        return unmodifiableList(columns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnDeleteQuery)) {
            return false;
        }
        ColumnDeleteQuery that = (ColumnDeleteQuery) o;
        return Objects.equals(columnFamily, that.getColumnFamily()) &&
                Objects.equals(condition, that.getCondition().orElse(null)) &&
                Objects.equals(columns, that.getColumns());
    }

    @Override
    public int hashCode() {
        return Objects.hash(columnFamily, condition, columns);
    }

    @Override
    public String toString() {
        return  "DefaultColumnDeleteQuery{" + "columnFamily='" + columnFamily + '\'' +
                ", condition=" + condition +
                ", columns=" + columns +
                '}';
    }
}
