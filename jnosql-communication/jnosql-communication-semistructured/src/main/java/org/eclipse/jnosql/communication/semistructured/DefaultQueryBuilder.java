/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;


import jakarta.data.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

class DefaultQueryBuilder implements SelectQuery.QueryBuilder {

    private final List<String> columns = new ArrayList<>();

    private final List<Sort<?>> sorts = new ArrayList<>();

    private String entity;

    private CriteriaCondition condition;

    private long skip;

    private long limit;


    @Override
    public SelectQuery.QueryBuilder select(String column) {
        Objects.requireNonNull(column, "column is required");
        this.columns.add(column);
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder select(String... columns) {
        Consumer<String> validNull = d -> requireNonNull(d, "there is null column in the query");
        Consumer<String> consume = this.columns::add;
        Stream.of(columns).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder sort(Sort<?> sort) {
        Objects.requireNonNull(sort, "sort is required");
        this.sorts.add(sort);
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder sort(Sort<?>... sorts) {
        Consumer<Sort<?>> validNull = d -> requireNonNull(d, "there is null document in the query");
        Consumer<Sort<?>> consume = this.sorts::add;
        Stream.of(sorts).forEach(validNull.andThen(consume));
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder from(String entity) {
        Objects.requireNonNull(entity, "entity is required");
        this.entity = entity;
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder where(CriteriaCondition condition) {
        Objects.requireNonNull(condition, "condition is required");
        this.condition = condition;
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder skip(long skip) {
        if (skip < 0) {
            throw new IllegalArgumentException("The skip should not be negative, skip: " + skip);
        }
        this.skip = skip;
        return this;
    }

    @Override
    public SelectQuery.QueryBuilder limit(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("The limit should not be negative, limit: " + limit);
        }
        this.limit = limit;
        return this;
    }

    @Override
    public SelectQuery build() {
        if (Objects.isNull(entity)) {
            throw new IllegalArgumentException("The document collection is mandatory to build");
        }
        return new DefaultSelectQuery(limit, skip, entity,
                columns, sorts, condition);
    }

    @Override
    public Stream<CommunicationEntity> getResult(DatabaseManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return manager.select(build());
    }

    @Override
    public Optional<CommunicationEntity> getSingleResult(DatabaseManager manager) {
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
        DefaultQueryBuilder that = (DefaultQueryBuilder) o;
        return skip == that.skip
                && limit == that.limit
                && Objects.equals(columns, that.columns)
                && Objects.equals(sorts, that.sorts)
                && Objects.equals(entity, that.entity)
                && Objects.equals(condition, that.condition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, sorts, entity, condition, skip, limit);
    }

    @Override
    public String toString() {
        return "DefaultColumnQueryBuilder{" +
                "columns=" + columns +
                ", sorts=" + sorts +
                ", entity='" + entity + '\'' +
                ", condition=" + condition +
                ", skip=" + skip +
                ", limit=" + limit +
                '}';
    }
}
