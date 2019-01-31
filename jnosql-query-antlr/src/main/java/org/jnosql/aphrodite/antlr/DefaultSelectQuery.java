/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.jnosql.aphrodite.antlr;

import org.jnosql.query.SelectQuery;
import org.jnosql.query.Sort;
import org.jnosql.query.Where;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

final class DefaultSelectQuery implements SelectQuery {

    private final String entity;

    private final List<String> fields;

    private final List<Sort> sorts;

    private long skip;

    private long limit;

    private final Where where;

    DefaultSelectQuery(String entity, List<String> fields, List<Sort> sorts, long skip, long limit, Where where) {
        this.entity = entity;
        this.fields = fields;
        this.sorts = sorts;
        this.skip = skip;
        this.limit = limit;
        this.where = where;
    }

    @Override
    public List<String> getFields() {
        return fields;
    }

    @Override
    public String getEntity() {
        return entity;
    }

    @Override
    public Optional<Where> getWhere() {
        return Optional.ofNullable(where);
    }

    @Override
    public long getSkip() {
        return skip;
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public List<Sort> getOrderBy() {
        return sorts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DefaultSelectQuery)) {
            return false;
        }
        DefaultSelectQuery that = (DefaultSelectQuery) o;
        return skip == that.skip &&
                limit == that.limit &&
                Objects.equals(entity, that.entity) &&
                Objects.equals(fields, that.fields) &&
                Objects.equals(sorts, that.sorts) &&
                Objects.equals(where, that.where);
    }

    @Override
    public int hashCode() {
        return Objects.hash(entity, fields, sorts, skip, limit, where);
    }
}
