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

import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import jakarta.nosql.mapping.column.ColumnQueryPagination.ColumnQueryPaginationProvider;

import java.util.Objects;

/**
 * The default implementation of {@link ColumnQueryPaginationProvider}
 */
public final class DefaultColumnQueryPaginationProvider implements ColumnQueryPaginationProvider {

    @Override
    public ColumnQueryPagination apply(ColumnQuery columnQuery, Pagination pagination) {
        Objects.requireNonNull(columnQuery, "columnQuery is requried");
        Objects.requireNonNull(pagination, "pagination is requried");
        return new DefaultColumnQueryPagination(columnQuery, pagination);
    }
}
