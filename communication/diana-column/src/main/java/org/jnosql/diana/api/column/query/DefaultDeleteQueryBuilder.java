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
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;

import java.util.List;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation to Delete query
 */
class DefaultDeleteQueryBuilder extends BaseQueryBuilder implements ColumnDelete, ColumnDeleteFrom,
        ColumnDeleteWhere, ColumnDeleteNotCondition {

    private String columnFamily;


    private final List<String> columns;


    DefaultDeleteQueryBuilder(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public ColumnDeleteFrom from(String columnFamily) {
        requireNonNull(columnFamily, "columnFamily is required");
        this.columnFamily = columnFamily;
        return this;
    }


    @Override
    public ColumnDeleteNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public ColumnDeleteNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public ColumnDeleteNameCondition or(String name) {
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
    public void execute(ColumnFamilyManager manager) {
        requireNonNull(manager, "manager is required");
        manager.delete(this.build());
    }

    @Override
    public void execute(ColumnFamilyManagerAsync manager) {
        requireNonNull(manager, "manager is required");
        manager.delete(this.build());
    }

    @Override
    public void execute(ColumnFamilyManagerAsync manager, Consumer<Void> callback) {
        requireNonNull(manager, "manager is required");
        requireNonNull(callback, "callback is required");
        manager.delete(this.build(), callback);
    }
}

