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
package org.jnosql.diana.column.query;


import org.jnosql.diana.Sort;
import org.jnosql.diana.column.ColumnCondition;
import org.jnosql.diana.column.ColumnQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.unmodifiableList;
import static java.util.Optional.ofNullable;

/**
 * The default implementation of column query.
 */
class DefaultColumnQuery implements ColumnQuery {

    private final long maxResults;

    private final long firstResult;

    private final String columnFamily;

    private final List<String> columns;

    private final List<Sort> sorts;

    private final ColumnCondition condition;


    DefaultColumnQuery(long maxResults, long firstResult, String columnFamily, List<String> columns, List<Sort> sorts, ColumnCondition condition) {
        this.maxResults = maxResults;
        this.firstResult = firstResult;
        this.columnFamily = columnFamily;
        this.columns = columns;
        this.sorts = sorts;
        this.condition = ofNullable(condition).map(ReadOnlyColumnCondition::new).orElse(null);
    }

    @Override
    public long getLimit() {
        return maxResults;
    }

    @Override
    public long getSkip() {
        return firstResult;
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
    public List<Sort> getSorts() {
        return unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnQuery)) {
            return false;
        }
        ColumnQuery that = (ColumnQuery) o;
        return maxResults == that.getLimit() &&
                firstResult == that.getSkip() &&
                Objects.equals(columnFamily, that.getColumnFamily()) &&
                Objects.equals(columns, that.getColumns()) &&
                Objects.equals(sorts, that.getSorts()) &&
                Objects.equals(condition, that.getCondition().orElse(null));
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxResults, firstResult, columnFamily, columns, sorts, condition);
    }

    @Override
    public String toString() {
        return  "DefaultColumnQuery{" + "maxResults=" + maxResults +
                ", firstResult=" + firstResult +
                ", columnFamily='" + columnFamily + '\'' +
                ", columns=" + columns +
                ", sorts=" + sorts +
                ", condition=" + condition +
                '}';
    }
}
