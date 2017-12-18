/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.ColumnQuery;

import static java.util.Objects.requireNonNull;

class DefaultColumnFromOrder implements ColumnFromOrder, ColumnNameOrder {



    private final DefaultSelectQueryBuilder queryBuilder;

    DefaultColumnFromOrder(DefaultSelectQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public ColumnNameOrder asc() {
        this.queryBuilder.sorts.add(Sort.of(queryBuilder.name, Sort.SortType.ASC));
        return this;
    }

    @Override
    public ColumnNameOrder desc() {
        this.queryBuilder.sorts.add(Sort.of(queryBuilder.name, Sort.SortType.DESC));
        return this;
    }

    @Override
    public ColumnFromOrder orderBy(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        return this;
    }

    @Override
    public ColumnStart start(long start) {
        this.queryBuilder.start(start);
        return queryBuilder;
    }

    @Override
    public ColumnLimit limit(long limit) {
        this.queryBuilder.limit(limit);
        return queryBuilder;
    }

    @Override
    public ColumnQuery build() {
        return queryBuilder.build();
    }
}
