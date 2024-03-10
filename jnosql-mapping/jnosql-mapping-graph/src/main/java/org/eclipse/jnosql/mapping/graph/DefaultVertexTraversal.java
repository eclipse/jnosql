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
package org.eclipse.jnosql.mapping.graph;

import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.nosql.Entity;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.graph.CommunicationEntityConverter;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * The default implementation of {@link VertexTraversal}
 */
class DefaultVertexTraversal extends AbstractVertexTraversal implements VertexTraversal {


    private static final Predicate<String> IS_EMPTY = String::isEmpty;
    private static final Predicate<String> NOT_EMPTY = IS_EMPTY.negate();

    DefaultVertexTraversal(Supplier<GraphTraversal<?, ?>> supplier,
                           Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Vertex>> flow,
                           EntityConverter converter) {
        super(supplier, flow, converter);
    }

    @Override
    public VertexTraversal has(String propertyKey, Object value) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(value, "value is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.has(propertyKey, value)), converter);
    }

    @Override
    public VertexTraversal has(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.has(propertyKey)), converter);
    }

    @Override
    public VertexTraversal has(String propertyKey, P<?> predicate) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(predicate, "predicate is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.has(propertyKey, predicate)), converter);
    }

    @Override
    public VertexTraversal has(T accessor, Object value) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(value, "value is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.has(accessor, value)), converter);
    }

    @Override
    public VertexTraversal has(T accessor, P<?> predicate) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(predicate, "predicate is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.has(accessor, predicate)), converter);
    }

    @Override
    public VertexTraversal out(String... labels) {
        Stream.of(labels).forEach(l -> requireNonNull(l, "label is required"));
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.out(labels)), converter);
    }

    @Override
    public <T> VertexTraversal filter(Predicate<T> predicate) {
        requireNonNull(predicate, "predicate is required");

        Predicate<Traverser<Vertex>> p = v -> predicate.test(GraphEntityConverter.INSTANCE.toEntity(converter, v.get()));
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.filter(p)), converter);
    }

    @Override
    public EdgeTraversal outE(String... edgeLabels) {
        Stream.of(edgeLabels).forEach(l -> requireNonNull(l, "label is required"));
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.outE(edgeLabels)), converter);
    }

    @Override
    public VertexTraversal in(String... labels) {
        Stream.of(labels).forEach(l -> requireNonNull(l, "label is required"));
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.in(labels)), converter);
    }

    @Override
    public EdgeTraversal inE(String... edgeLabels) {
        Stream.of(edgeLabels).forEach(l -> requireNonNull(l, "edgeLabel is required"));

        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.inE(edgeLabels)), converter);
    }

    @Override
    public VertexTraversal both(String... labels) {
        Stream.of(labels).forEach(l -> requireNonNull(l, "labels is required"));
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.both(labels)), converter);
    }

    @Override
    public EdgeTraversal bothE(String... edgeLabels) {
        Stream.of(edgeLabels).forEach(l -> requireNonNull(l, "edgeLabel is required"));
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.bothE(edgeLabels)), converter);
    }

    @Override
    public VertexTraversal dedup(String... labels) {
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.dedup(labels)), converter);
    }

    @Override
    public VertexRepeatTraversal repeat() {
        return new DefaultVertexRepeatTraversal(supplier, flow, converter);
    }

    @Override
    public VertexTraversal limit(long limit) {
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.limit(limit)), converter);
    }

    @Override
    public VertexTraversal skip(long skip) {
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.skip(skip)), converter);
    }

    @Override
    public VertexTraversal range(long start, long end) {
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.range(start, end)), converter);
    }


    @Override
    public VertexTraversal hasLabel(String label) {
        requireNonNull(label, "label is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.hasLabel(label)), converter);
    }

    @Override
    public <T> VertexTraversal hasLabel(Class<T> type) {
        requireNonNull(type, "type is required");
        Entity entity = type.getAnnotation(Entity.class);
        String label = Optional.ofNullable(entity).map(Entity::value)
                .filter(NOT_EMPTY)
                .orElse(type.getSimpleName());
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.hasLabel(label)), converter);
    }

    @Override
    public <T> VertexTraversal hasLabel(P<String> predicate) {
        requireNonNull(predicate, "predicate is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.hasLabel(predicate)), converter);
    }

    @Override
    public VertexTraversal hasNot(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.hasNot(propertyKey)), converter);
    }

    @Override
    public <T> Optional<T> next() {
        Optional<Vertex> vertex = flow.apply(supplier.get()).tryNext();
        return vertex.map(CommunicationEntityConverter.INSTANCE).map(converter::toEntity);
    }

    @Override
    public <T> Stream<T> result() {
        return flow.apply(supplier.get())
                .toStream()
                .map(CommunicationEntityConverter.INSTANCE)
                .map(converter::toEntity);
    }

    @Override
    public <T> Optional<T> singleResult() {
        final Stream<T> stream = result();
        final Iterator<T> iterator = stream.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final T entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("The Vertex traversal query returns more than one result");
    }

    @Override
    public <T> Stream<T> next(int limit) {
        return flow.apply(supplier.get())
                .next(limit)
                .stream()
                .map(CommunicationEntityConverter.INSTANCE)
                .map(converter::toEntity);
    }

    @Override
    public ValueMapTraversal valueMap(String... propertyKeys) {
        return new DefaultValueMapTraversal(supplier, flow.andThen(g -> g.valueMap(false, propertyKeys)));
    }

    @Override
    public long count() {
        return flow.apply(supplier.get()).count().tryNext().orElse(0L);
    }

    @Override
    public VertexTraversalOrder orderBy(String property) {
        requireNonNull(property, "property is required");
        return new DefaultVertexTraversalOrder(supplier, flow, converter, property);
    }
}
