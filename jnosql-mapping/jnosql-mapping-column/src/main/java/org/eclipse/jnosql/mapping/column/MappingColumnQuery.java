/*
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
 */
package org.eclipse.jnosql.mapping.column;


import jakarta.data.repository.Sort;
import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.emptyList;

/**
 * A mapping implementation of {@link ColumnQuery}
 */
public final class MappingColumnQuery implements ColumnQuery {

    private final List<Sort> sorts;
    private final long limit;
    private final long skip;
    private final ColumnCondition condition;
    private final String columnFamily;

    public MappingColumnQuery(List<Sort> sorts, long limit, long skip, ColumnCondition condition, String columnFamily) {
        this.sorts = sorts;
        this.limit = limit;
        this.skip = skip;
        this.condition = condition;
        this.columnFamily = columnFamily;
    }

    @Override
    public long limit() {
        return limit;
    }

    @Override
    public long skip() {
        return skip;
    }

    @Override
    public String name() {
        return columnFamily;
    }

    @Override
    public Optional<ColumnCondition> condition() {
        return Optional.ofNullable(condition);
    }

    @Override
    public List<String> columns() {
        return emptyList();
    }

    @Override
    public List<Sort> sorts() {
        return sorts;
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
        return limit == that.limit()
                && skip == that.skip()
                && Objects.equals(sorts, that.sorts())
                && Objects.equals(condition, that.condition().orElse(null))
                && Objects.equals(columnFamily, that.name());
    }

    @Override
    public int hashCode() {
        return Objects.hash(limit, skip, columnFamily, emptyList(), sorts, condition);
    }

    @Override
    public String toString() {
        return  "ArtemisColumnQuery{" + "limit=" + limit +
                ", skip=" + skip +
                ", columnFamily='" + columnFamily + '\'' +
                ", columns=" + emptyList() +
                ", sorts=" + sorts +
                ", condition=" + condition +
                '}';
    }
}
