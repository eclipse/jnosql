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
package org.eclipse.jnosql.communication.column;


import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnDeleteQuery.ColumnDelete;
import jakarta.nosql.column.ColumnDeleteQuery.ColumnDeleteFrom;
import jakarta.nosql.column.ColumnDeleteQuery.ColumnDeleteNotCondition;
import jakarta.nosql.column.ColumnDeleteQuery.ColumnDeleteWhere;
import jakarta.nosql.column.ColumnManager;

import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation to Delete query
 */
class DefaultFluentDeleteQueryBuilder extends BaseQueryBuilder implements ColumnDelete, ColumnDeleteFrom,
        ColumnDeleteWhere, ColumnDeleteNotCondition {

    private String columnFamily;


    private final List<String> columns;


    DefaultFluentDeleteQueryBuilder(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public ColumnDeleteFrom from(String columnFamily) {
        requireNonNull(columnFamily, "columnFamily is required");
        this.columnFamily = columnFamily;
        return this;
    }


    @Override
    public ColumnDeleteQuery.ColumnDeleteNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public ColumnDeleteQuery.ColumnDeleteNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public ColumnDeleteQuery.ColumnDeleteNameCondition or(String name) {
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
    public <T> ColumnDeleteWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public ColumnDeleteWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere lt(T value) {
        ltImpl(value);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> ColumnDeleteWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }

    @Override
    public ColumnDeleteQuery build() {
        return new DefaultColumnDeleteQuery(columnFamily, condition, columns);
    }

    @Override
    public void delete(ColumnManager manager) {
        requireNonNull(manager, "manager is required");
        manager.delete(this.build());
    }

}