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

import org.jnosql.artemis.Page;
import org.jnosql.artemis.Pagination;
import org.jnosql.diana.column.ColumnQuery;

import java.util.Objects;

/**
 * A {@link ColumnQuery} that allows select based on pagination.
 */
public interface ColumnQueryPagination extends ColumnQuery {


    /**
     * Returns the {@link ColumnQueryPagination} requesting the next {@link ColumnQueryPagination}.
     *
     * @return the next {@link ColumnQueryPagination}
     */
    ColumnQueryPagination next();

    /**
     * Returns the {@link Pagination} of the current {@link Page}
     *
     * @return a current {@link Pagination}
     */
    Pagination getPagination();


    /**
     * Creates a new instance of {@link ColumnQueryPagination}
     *
     * @param query      the query
     * @param pagination the pagination
     * @return a {@link ColumnQueryPagination} instance
     * @throws NullPointerException when there is null parameter
     */
    static ColumnQueryPagination of(ColumnQuery query, Pagination pagination) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(pagination, "pagination is required");
        return new DefaultColumnQueryPagination(query, pagination);
    }
}
