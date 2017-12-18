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

import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnQuery;

import static java.util.Objects.requireNonNull;

class DefaultColumnWhere implements ColumnWhere {

    private final DefaultSelectQueryBuilder queryBuilder;

    DefaultColumnWhere(DefaultSelectQueryBuilder queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public ColumnWhere and(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.queryBuilder.condition = this.queryBuilder.condition.and(condition);
        return this;
    }

    @Override
    public ColumnWhere or(ColumnCondition condition) throws NullPointerException {
        requireNonNull(condition, "condition is required");
        this.queryBuilder.condition = this.queryBuilder.condition.or(condition);
        return this;
    }

    @Override
    public ColumnNameCondition and(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.queryBuilder.name = name;
        this.queryBuilder.and = true;
        return queryBuilder;
    }

    @Override
    public ColumnNameCondition or(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.queryBuilder.name = name;
        this.queryBuilder.and = false;
        return queryBuilder;
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
    public ColumnOrder orderBy(String name) throws NullPointerException {
        this.queryBuilder.orderBy(name);
        return queryBuilder;
    }

    @Override
    public ColumnQuery build() {
        return queryBuilder.build();
    }
}
