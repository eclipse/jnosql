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


import jakarta.data.repository.Sort;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnFrom;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnLimit;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnNameCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnNameOrder;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnNotCondition;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnOrder;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnQueryBuild;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnSelect;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnSkip;
import org.eclipse.jnosql.communication.column.ColumnQuery.ColumnWhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of the Select in the column
 */
class DefaultFluentColumnQueryBuilder extends BaseQueryBuilder implements
        ColumnSelect, ColumnFrom, ColumnLimit, ColumnSkip,
        ColumnOrder, ColumnNameCondition, ColumnNotCondition, ColumnNameOrder, ColumnWhere, ColumnQueryBuild {


    private String columnFamily;

    private long skip;

    private long limit;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> columns;


    DefaultFluentColumnQueryBuilder(List<String> columns) {
        this.columns = columns;
    }


    @Override
    public ColumnFrom from(String columnFamily) {
        requireNonNull(columnFamily, "columnFamily is required");
        this.columnFamily = columnFamily;
        return this;
    }


    @Override
    public ColumnNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public ColumnNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public ColumnNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }

    @Override
    public ColumnSkip skip(long skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("The skip should not be negative, skip: " + skip);
        }
        this.skip = skip;
        return this;
    }

    @Override
    public ColumnLimit limit(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("The limit should not be negative, limit: " + limit);
        }
        this.limit = limit;
        return this;
    }

    @Override
    public ColumnOrder orderBy(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public ColumnNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> ColumnWhere eq(T value) {
        eqImpl(value);
        return this;
    }

    @Override
    public ColumnWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> ColumnWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> ColumnWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnWhere lt(T value) {
        ltImpl(value);
        return this;
    }


    @Override
    public <T> ColumnWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }


    @Override
    public <T> ColumnWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }


    @Override
    public ColumnNameOrder asc() {
        this.sorts.add(Sort.asc(name));
        return this;
    }

    @Override
    public ColumnNameOrder desc() {
        this.sorts.add(Sort.desc(name));
        return this;
    }


    @Override
    public ColumnQuery build() {
        return new DefaultColumnQuery(limit, skip, columnFamily, columns, sorts, condition);
    }

    @Override
    public Stream<ColumnEntity> getResult(ColumnManager manager) {
        requireNonNull(manager, "manager is required");
        return manager.select(this.build());
    }

    @Override
    public Optional<ColumnEntity> getSingleResult(ColumnManager manager) {
        requireNonNull(manager, "manager is required");
        return manager.singleResult(this.build());
    }

}
