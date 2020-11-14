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
package org.eclipse.jnosql.mapping;

import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.Pagination.PaginationBuilder;

/**
 * The builder of {@link Pagination}
 */
final class DefaultPaginationBuilder implements PaginationBuilder {

    private final long page;

    DefaultPaginationBuilder(long page) {
        this.page = page;
    }

    @Override
    public Pagination size(long size) {
        if (size < 1) {
            throw new IllegalArgumentException("A pagination size cannot be zero or negative");
        }
        return new DefaultPagination(page, size);
    }
}
