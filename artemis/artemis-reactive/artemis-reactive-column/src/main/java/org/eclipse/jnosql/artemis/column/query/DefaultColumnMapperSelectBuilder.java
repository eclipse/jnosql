/*
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
 */
package org.eclipse.jnosql.artemis.column.query;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperFrom;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperLimit;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperNameCondition;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperNameOrder;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperNotCondition;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperOrder;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperSkip;
import jakarta.nosql.mapping.column.ColumnQueryMapper.ColumnMapperWhere;
import jakarta.nosql.mapping.column.ColumnQueryPagination;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.reflection.ClassMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

class DefaultColumnMapperSelectBuilder extends AbstractMapperQuery implements ColumnMapperFrom,
        ColumnMapperLimit, ColumnMapperSkip,
        ColumnMapperOrder, ColumnMapperNameCondition,
        ColumnMapperNotCondition, ColumnMapperNameOrder,
        ColumnMapperWhere {

    private final List<Sort> sorts = new ArrayList<>();

    DefaultColumnMapperSelectBuilder(ClassMapping mapping, Converters converters) {
        super(mapping, converters);
    }

    @Override
    public ColumnMapperNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public ColumnMapperNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }

    @Override
    public ColumnMapperNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public ColumnMapperSkip skip(long skip) {
        this.start = skip;
        return this;
    }

    @Override
    public ColumnMapperLimit limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public ColumnMapperOrder orderBy(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }


    @Override
    public ColumnMapperNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> ColumnMapperWhere eq(T value) {
        eqImpl(value);
        return this;
    }


    @Override
    public ColumnMapperWhere like(String value) {
        likeImpl(value);
        return this;
    }


    @Override
    public <T> ColumnMapperWhere gt(T value) {
        gtImpl(value);
        return this;
    }


    @Override
    public <T> ColumnMapperWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> ColumnMapperWhere lt(T value) {
        ltImpl(value);
        return this;
    }


    @Override
    public <T> ColumnMapperWhere lte(T value) {
        lteImpl(value);
        return this;
    }


    @Override
    public <T> ColumnMapperWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> ColumnMapperWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }

    @Override
    public ColumnMapperNameOrder asc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), SortType.ASC));
        return this;
    }

    @Override
    public ColumnMapperNameOrder desc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), SortType.DESC));
        return this;
    }


    @Override
    public ColumnQuery build() {
        return new ArtemisColumnQuery(sorts, limit, start, condition, columnFamily);
    }

    @Override
    public ColumnQuery build(Pagination pagination) {
        requireNonNull(pagination, "pagination is required");
        return ColumnQueryPagination.of(build(), pagination);
    }

    @Override
    public <T> Stream<T> getResult(ColumnTemplate template) {
        requireNonNull(template, "template is required");
        return template.select(this.build());
    }

    @Override
    public <T> Optional<T> getSingleResult(ColumnTemplate template) {
        requireNonNull(template, "template is required");
        return template.singleResult(this.build());
    }

    @Override
    public <T> Stream<T> getResult(ColumnTemplate template, Pagination pagination) {
        requireNonNull(template, "template is required");
        requireNonNull(pagination, "pagination is required");
        return template.select(this.build(pagination));
    }

    @Override
    public <T> Optional<T> getSingleResult(ColumnTemplate template, Pagination pagination) {
        requireNonNull(template, "template is required");
        requireNonNull(pagination, "pagination is required");
        return template.singleResult(this.build(pagination));
    }

    @Override
    public <T> Page<T> page(ColumnTemplate template, Pagination pagination) {
        requireNonNull(pagination, "pagination is required");
        requireNonNull(template, "template is required");
        return template.select(ColumnQueryPagination.of(build(), pagination));
    }

}
