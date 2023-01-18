/*
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
 */
package org.eclipse.jnosql.mapping.document;

import jakarta.data.repository.Direction;
import jakarta.data.repository.Sort;
import jakarta.nosql.QueryMapper.MapperFrom;
import jakarta.nosql.QueryMapper.MapperLimit;
import jakarta.nosql.QueryMapper.MapperNameCondition;
import jakarta.nosql.QueryMapper.MapperNameOrder;
import jakarta.nosql.QueryMapper.MapperNotCondition;
import jakarta.nosql.QueryMapper.MapperOrder;
import jakarta.nosql.QueryMapper.MapperSkip;
import jakarta.nosql.QueryMapper.MapperWhere;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

final class DocumentMapperSelect extends AbstractMapperQuery implements MapperFrom, MapperLimit,
        MapperSkip, MapperOrder, MapperNameCondition,
        MapperNotCondition, MapperNameOrder, MapperWhere {

    private final List<Sort> sorts = new ArrayList<>();


    DocumentMapperSelect(EntityMetadata mapping, Converters converters, JNoSQLDocumentTemplate template) {
        super(mapping, converters, template);
    }


    @Override
    public MapperNameCondition and(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = true;
        return this;
    }

    @Override
    public MapperNameCondition or(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        this.and = false;
        return this;
    }

    @Override
    public MapperNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public MapperSkip skip(long start) {
        this.start = start;
        return this;
    }

    @Override
    public MapperLimit limit(long limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public MapperOrder orderBy(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
    }

    @Override
    public MapperNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public <T> MapperWhere eq(T value) {
        eqImpl(value);
        return this;
    }


    @Override
    public MapperWhere like(String value) {
        likeImpl(value);
        return this;
    }

    @Override
    public <T> MapperWhere gt(T value) {
        gtImpl(value);
        return this;
    }

    @Override
    public <T> MapperWhere gte(T value) {
        gteImpl(value);
        return this;
    }

    @Override
    public <T> MapperWhere lt(T value) {
        ltImpl(value);
        return this;
    }


    @Override
    public <T> MapperWhere lte(T value) {
        lteImpl(value);
        return this;
    }

    @Override
    public <T> MapperWhere between(T valueA, T valueB) {
        betweenImpl(valueA, valueB);
        return this;
    }

    @Override
    public <T> MapperWhere in(Iterable<T> values) {
        inImpl(values);
        return this;
    }

    @Override
    public MapperNameOrder asc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), Direction.ASC, false));
        return this;
    }

    @Override
    public MapperNameOrder desc() {
        this.sorts.add(Sort.of(mapping.getColumnField(name), Direction.DESC, false));
        return this;
    }

    private DocumentQuery build() {
        return new MappingDocumentQuery(sorts, limit, start, condition, documentCollection);
    }

    @Override
    public <T> List<T> result() {
        DocumentQuery query = build();
        return this.template.<T>select(query)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> Stream<T> stream() {
        DocumentQuery query = build();
        return this.template.select(query);
    }

    @Override
    public <T> Optional<T> singleResult() {
        DocumentQuery query = build();
        return this.template.singleResult(query);
    }
}
