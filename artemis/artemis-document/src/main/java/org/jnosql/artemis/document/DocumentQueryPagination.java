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
package org.jnosql.artemis.document;

import jakarta.nosql.mapping.Page;
import org.jnosql.artemis.Pagination;
import org.jnosql.diana.document.DocumentQuery;

import java.util.Objects;

/**
 * A {@link DocumentQuery} that allows select based on pagination.
 */
public interface DocumentQueryPagination extends DocumentQuery {

    /**
     * Returns the {@link DocumentQueryPagination} requesting the next {@link DocumentQueryPagination}.
     *
     * @return the next {@link DocumentQueryPagination}
     */
    DocumentQueryPagination next();

    /**
     * Returns the {@link Pagination} of the current {@link Page}
     *
     * @return a current {@link Pagination}
     */
    Pagination getPagination();


    /**
     * Creates a new instance of {@link DocumentQueryPagination}
     *
     * @param query      the query
     * @param pagination the pagination
     * @return a {@link DocumentQueryPagination} instance
     * @throws NullPointerException when there is null parameter
     */
    static DocumentQueryPagination of(DocumentQuery query, Pagination pagination) {
        Objects.requireNonNull(query, "query is required");
        Objects.requireNonNull(pagination, "pagination is required");
        return new DefaultDocumentQueryPagination(query, pagination);
    }
}
