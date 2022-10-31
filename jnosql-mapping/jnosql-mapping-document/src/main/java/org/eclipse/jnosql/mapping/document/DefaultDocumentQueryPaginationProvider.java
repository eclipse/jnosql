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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentQueryPagination.DocumentQueryPaginationProvider;

import java.util.Objects;

/**
 * implementation of {@link DocumentQueryPaginationProvider}
 */
public final class DefaultDocumentQueryPaginationProvider implements DocumentQueryPaginationProvider {

    @Override
    public DocumentQueryPagination apply(DocumentQuery documentQuery, Pagination pagination) {
        Objects.requireNonNull(pagination, "pagination is required");
        Objects.requireNonNull(documentQuery, "documentQuery is required");
        return new DefaultDocumentQueryPagination(documentQuery, pagination);
    }
}
