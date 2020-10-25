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
package org.eclipse.jnosql.communication.column.query;


import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQuery.ColumnFrom;
import jakarta.nosql.column.ColumnQuery.ColumnLimit;
import jakarta.nosql.column.ColumnQuery.ColumnNameCondition;
import jakarta.nosql.column.ColumnQuery.ColumnNameOrder;
import jakarta.nosql.column.ColumnQuery.ColumnNotCondition;
import jakarta.nosql.column.ColumnQuery.ColumnOrder;
import jakarta.nosql.column.ColumnQuery.ColumnQueryBuild;
import jakarta.nosql.column.ColumnQuery.ColumnSelect;
import jakarta.nosql.column.ColumnQuery.ColumnSkip;
import jakarta.nosql.column.ColumnQuery.ColumnWhere;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of the Select in the column
 */
class DefaultSelectQueryBuilder extends BaseQueryBuilder implements
        ColumnSelect, ColumnFrom, ColumnLimit, ColumnSkip,
        ColumnOrder, ColumnNameCondition, ColumnNotCondition, ColumnNameOrder, ColumnWhere, ColumnQueryBuild {


    private String columnFamily;

    private long skip;

    private long limit;

    private final List<Sort> sorts = new ArrayList<>();

    private final List<String> columns;


    DefaultSelectQueryBuilder(List<String> columns) {
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
        this.skip = skip;
        return this;
    }

    @Override
    public ColumnLimit limit(long limit) {
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
        this.sorts.add(Sort.of(name, SortType.ASC));
        return this;
    }

    @Override
    public ColumnNameOrder desc() {
        this.sorts.add(Sort.of(name, SortType.DESC));
        return this;
    }


    @Override
    public ColumnQuery build() {
        return new DefaultColumnQuery(limit, skip, columnFamily, columns, sorts, condition);
    }

    @Override
    public Stream<ColumnEntity> getResult(ColumnFamilyManager manager) {
        requireNonNull(manager, "manager is required");
        return manager.select(this.build());
    }

    @Override
    public Optional<ColumnEntity> getSingleResult(ColumnFamilyManager manager) {
        requireNonNull(manager, "manager is required");
        return manager.singleResult(this.build());
    }

}
