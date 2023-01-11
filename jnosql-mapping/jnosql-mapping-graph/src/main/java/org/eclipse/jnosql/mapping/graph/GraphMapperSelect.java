/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.mapping.graph;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.QueryMapper.MapperFrom;
import jakarta.nosql.mapping.QueryMapper.MapperLimit;
import jakarta.nosql.mapping.QueryMapper.MapperNameCondition;
import jakarta.nosql.mapping.QueryMapper.MapperNameOrder;
import jakarta.nosql.mapping.QueryMapper.MapperNotCondition;
import jakarta.nosql.mapping.QueryMapper.MapperOrder;
import jakarta.nosql.mapping.QueryMapper.MapperSkip;
import jakarta.nosql.mapping.QueryMapper.MapperWhere;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.asc;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;

final class GraphMapperSelect extends AbstractMapperQuery
        implements MapperFrom, MapperLimit,
        MapperSkip, MapperOrder, MapperNameCondition,
        MapperNotCondition, MapperNameOrder, MapperWhere {


    GraphMapperSelect(EntityMetadata mapping, Converters converters,
                      GraphTraversal<Vertex, Vertex> traversal, GraphConverter converter) {
        super(mapping, converters, traversal, converter);
    }

    @Override
    public MapperNameCondition where(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
        return this;
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
    public MapperSkip skip(long start) {
        this.traversal.skip(start);
        return this;
    }

    @Override
    public MapperLimit limit(long limit) {
        this.traversal.limit(limit);
        return this;
    }

    @Override
    public MapperOrder orderBy(String name) {
        requireNonNull(name, "name is required");
        this.name = name;
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
        return null;
    }

    @Override
    public MapperNotCondition not() {
        this.negate = true;
        return this;
    }

    @Override
    public MapperNameOrder asc() {
        traversal.order().by(mapping.getColumnField(mapping.getColumnField(name)), asc);
        return this;
    }

    @Override
    public MapperNameOrder desc() {
        traversal.order().by(mapping.getColumnField(mapping.getColumnField(name)), desc);
        return this;
    }

    @Override
    public <T> List<T> result() {
        Stream<T> stream = stream();
        return stream.collect(Collectors.toUnmodifiableList());
    }

    @Override
    public <T> Stream<T> stream() {
        if (condition != null) {
            traversal.filter(condition);
        }
        return traversal.toStream().map(converter::toEntity);
    }

    @Override
    public <T> Optional<T> singleResult() {
        List<T> result = result();
        if (result.isEmpty()) {
            return Optional.empty();
        }
        if (result.size() == 1) {
            return Optional.ofNullable(result.get(0));
        }
        throw new NonUniqueResultException("The query returns more than one result. Elements: " + result.size());
    }
}
