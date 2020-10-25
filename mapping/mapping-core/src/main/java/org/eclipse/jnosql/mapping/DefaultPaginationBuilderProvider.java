/*
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
 */
package org.eclipse.jnosql.mapping;

import jakarta.nosql.mapping.Pagination.PaginationBuilder;
import jakarta.nosql.mapping.Pagination.PaginationBuilderProvider;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link PaginationBuilderProvider}
 */
public final class DefaultPaginationBuilderProvider implements PaginationBuilderProvider {

    @Override
    public PaginationBuilder apply(Long page) {

        requireNonNull(page, "page is required");
        if (page <= 0) {
            throw new IllegalArgumentException("The page index cannot be less equals than zero.");
        }
        return new DefaultPaginationBuilder(page);
    }
}
