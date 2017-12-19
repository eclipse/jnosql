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


import org.jnosql.diana.api.column.ColumnDeleteQuery;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation to Delete query
 */
class DefaultDeleteQueryBuilder extends BaseQueryBuilder implements ColumnDelete, ColumnDeleteFrom,
        ColumnDeleteWhere, ColumnDeleteWhereName, ColumnDeleteNotCondition {

    private String columnFamily;


    private final List<String> columns;


    DefaultDeleteQueryBuilder(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public ColumnDeleteFrom from(String columnFamily) throws NullPointerException {
        requireNonNull(columnFamily, "columnFamily is required");
        this.columnFamily = columnFamily;
        return this;
    }


    @Override
    public ColumnDeleteWhereName where(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public ColumnDeleteNameCondition and(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public ColumnDeleteNameCondition or(String name) throws NullPointerException {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }


    @Override
    public ColumnDeleteNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere eq(T value) throws NullPointerException {
        eqImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere like(String value) throws NullPointerException {
        likeImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere gt(Number value) throws NullPointerException {
        gtImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere gte(Number value) throws NullPointerException {
        gteImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere lt(Number value) throws NullPointerException {
        ltImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere lte(Number value) throws NullPointerException {
        lteImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere between(Number valueA, Number valueB) throws NullPointerException {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere in(Iterable<T> values) throws NullPointerException {
        inImpl(values);
        return this;
    }


    @Override
    public ColumnDeleteQuery build() {
        return new DefaultColumnDeleteQuery(columnFamily, condition, columns);
    }
}

