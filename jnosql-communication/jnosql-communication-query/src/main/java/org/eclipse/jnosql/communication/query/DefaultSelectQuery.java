/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */

package org.eclipse.jnosql.communication.query;


import jakarta.data.repository.Sort;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * The default implementation of {@link SelectQuery}
 */
final class DefaultSelectQuery implements SelectQuery {

    private final String entity;

    private final List<String> fields;

    private final List<Sort> sorts;

    private final long skip;

    private final long limit;

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
    public List<String> fields() {
        return fields;
    }

    @Override
    public String entity() {
        return entity;
    }

    @Override
    public Optional<Where> where() {
        return Optional.ofNullable(where);
    }

    @Override
    public long skip() {
        return skip;
    }

    @Override
    public long limit() {
        return limit;
    }

    @Override
    public List<Sort> orderBy() {
        return sorts;
    }


    /**
     * Obtains an instance of {@link DefaultSelectQuery} from a text string.
     *
     * @param query the query
     * @return {@link DefaultSelectQuery} instance
     * @throws NullPointerException when the query is null
     */
    static DefaultSelectQuery parse(String query) {
        Objects.requireNonNull(query, "query is required");
        return new SelectQueryProvider().apply(query);
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
