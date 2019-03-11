/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.column;

import org.jnosql.artemis.Pagination;
import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnQuery;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class DefaultColumnQueryPagination implements ColumnQueryPagination {

    private final ColumnQuery query;

    private final Pagination pagination;

    DefaultColumnQueryPagination(ColumnQuery query, Pagination pagination) {
        this.query = query;
        this.pagination = pagination;
    }

    @Override
    public long getLimit() {
        return pagination.getLimit();
    }

    @Override
    public long getSkip() {
        return pagination.getSkip();
    }

    @Override
    public String getColumnFamily() {
        return query.getColumnFamily();
    }

    @Override
    public Optional<ColumnCondition> getCondition() {
        return query.getCondition();
    }

    @Override
    public List<String> getColumns() {
        return query.getColumns();
    }

    @Override
    public List<Sort> getSorts() {
        return query.getSorts();
    }

    @Override
    public ColumnQueryPagination next() {
        return new DefaultColumnQueryPagination(query, pagination.next());
    }

    @Override
    public Pagination getPagination() {
        return pagination;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultColumnQueryPagination that = (DefaultColumnQueryPagination) o;
        return Objects.equals(query, that.query) &&
                Objects.equals(pagination, that.pagination);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, pagination);
    }

    @Override
    public String toString() {
        return "DefaultColumnQueryPagination{" +
                "query=" + query +
                ", pagination=" + pagination +
                '}';
    }
}