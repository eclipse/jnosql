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



import jakarta.data.repository.Sort;

import java.util.Collections;
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


    DefaultColumnQuery(long maxResults, long firstResult, String columnFamily,
                       List<String> columns, List<Sort> sorts, ColumnCondition condition) {
        this.maxResults = maxResults;
        this.firstResult = firstResult;
        this.columnFamily = columnFamily;
        this.columns = columns;
        this.sorts = sorts;
        this.condition = ofNullable(condition).map(ColumnCondition::readOnly).orElse(null);
    }

    @Override
    public long limit() {
        return maxResults;
    }

    @Override
    public long skip() {
        return firstResult;
    }

    @Override
    public String name() {
        return columnFamily;
    }

    @Override
    public Optional<ColumnCondition> condition() {
        return ofNullable(condition);
    }

    @Override
    public List<String> columns() {
        return unmodifiableList(columns);
    }

    @Override
    public List<Sort> sorts() {
        return unmodifiableList(sorts);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ColumnQuery that)) {
            return false;
        }
        return maxResults == that.limit() &&
                firstResult == that.skip() &&
                Objects.equals(columnFamily, that.name()) &&
                Objects.equals(columns, that.columns()) &&
                Objects.equals(sorts, that.sorts()) &&
                Objects.equals(condition, that.condition().orElse(null));
    }

    @Override
    public int hashCode() {
        return Objects.hash(maxResults, firstResult, columnFamily, columns, sorts, condition);
    }

    @Override
    public String toString() {
        return  "ColumnQuery{" + "maxResults=" + maxResults +
                ", firstResult=" + firstResult +
                ", columnFamily='" + columnFamily + '\'' +
                ", columns=" + columns +
                ", sorts=" + sorts +
                ", condition=" + condition +
                '}';
    }
    static ColumnQuery countBy(ColumnQuery query) {
        return new DefaultColumnQuery(0, 0, query.name(), query.columns(),
                Collections.emptyList(), query.condition().orElse(null));
    }

    static ColumnQuery existsBy(ColumnQuery query) {
        return new DefaultColumnQuery(1, 0, query.name(), query.columns(),
                Collections.emptyList(), query.condition().orElse(null));
    }
}
