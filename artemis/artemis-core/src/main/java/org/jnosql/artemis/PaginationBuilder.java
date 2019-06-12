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
package org.jnosql.artemis;

import jakarta.nosql.mapping.Pagination;

/**
 * The builder of {@link Pagination}
 */
public class PaginationBuilder {

    private final long page;

    PaginationBuilder(long page) {
        this.page = page;
    }

    /**
     * Defines the size of a pagination
     *
     * @param size the size of pagination
     * @return a {@link Pagination} instance
     * @throws IllegalArgumentException when size is either zero or negative
     */
    public Pagination size(long size) {
        if (size < 1) {
            throw new IllegalArgumentException("A pagination size cannot be zero or negative");
        }
        return new DefaultPagination(page, size);
    }
}
