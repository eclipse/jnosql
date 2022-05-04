/*
 *
 *  Copyright (c) 2022 Otávio Santana and others
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
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

class DefaultColumnQueryBuilder implements ColumnQuery.ColumnQueryBuilder {

    private List<String> columns = new ArrayList<>();

    private List<Sort> sorts = new ArrayList<>();

    private String documentCollection;

    private ColumnCondition condition;

    private long skip;

    private long limit;


    @Override
    public ColumnQuery.ColumnQueryBuilder select(String column) {
        Objects.requireNonNull(column, "column is required");
        this.columns.add(column);
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder select(String... columns) {
        Consumer<String> validNull = d -> requireNonNull(d, "there is null column in the query");
        Consumer<String> consume = this.columns::add;
        Stream.of(columns).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder sort(Sort sort) {
        Objects.requireNonNull(sort, "sort is required");
        this.sorts.add(sort);
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder sort(Sort... sorts) {
        Consumer<Sort> validNull = d -> requireNonNull(d, "there is null document in the query");
        Consumer<Sort> consume = this.sorts::add;
        Stream.of(sorts).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder from(String documentCollection) {
        Objects.requireNonNull(documentCollection, "documentCollection is required");
        this.documentCollection = documentCollection;
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder where(ColumnCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder skip(long skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("The skip should not be negative, skip: " + skip);
        }
        this.skip = skip;
        return this;
    }

    @Override
    public ColumnQuery.ColumnQueryBuilder limit(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("The limit should not be negative, limit: " + limit);
        }
        this.limit = limit;
        return this;
    }

    @Override
    public ColumnQuery build() {
        if (Objects.isNull(documentCollection)) {
            throw new IllegalArgumentException("The document collection is mandatory to build");
        }
        return new DefaultColumnQuery(limit, skip, documentCollection,
                columns, sorts, condition);
    }

    @Override
    public Stream<ColumnEntity> getResult(ColumnFamilyManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return manager.select(build());
    }

    @Override
    public Optional<ColumnEntity> getSingleResult(ColumnFamilyManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return manager.singleResult(build());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DefaultColumnQueryBuilder that = (DefaultColumnQueryBuilder) o;
        return skip == that.skip
                && limit == that.limit
                && Objects.equals(columns, that.columns)
                && Objects.equals(sorts, that.sorts)
                && Objects.equals(documentCollection, that.documentCollection)
                && Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, sorts, documentCollection, condition, skip, limit);
    }

    @Override
    public String toString() {
        return "DefaultColumnQueryBuilder{" +
                "columns=" + columns +
                ", sorts=" + sorts +
                ", documentCollection='" + documentCollection + '\'' +
                ", condition=" + condition +
                ", skip=" + skip +
                ", limit=" + limit +
                '}';
    }
}
