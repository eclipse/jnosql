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
package org.eclipse.jnosql.artemis.graph;

import jakarta.nosql.NonUniqueResultException;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

class DefaultEdgeTraversal extends AbstractEdgeTraversal implements EdgeTraversal {


    DefaultEdgeTraversal(Supplier<GraphTraversal<?, ?>> supplier,
                         Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Edge>> flow,
                         GraphConverter converter) {
        super(supplier, flow, converter);
    }

    @Override
    public EdgeTraversal has(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.has(propertyKey)), converter);

    }

    @Override
    public EdgeTraversal has(String propertyKey, Object value) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(value, "value is required");

        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.has(propertyKey, value)), converter);
    }

    @Override
    public EdgeTraversal has(String propertyKey, P<?> predicate) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(predicate, "predicate is required");
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.has(propertyKey, predicate)), converter);
    }

    @Override
    public EdgeTraversal has(T accessor, Object value) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(value, "value is required");
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.has(accessor, value)), converter);
    }

    @Override
    public EdgeTraversal has(T accessor, P<?> predicate) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(predicate, "predicate is required");
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.has(accessor, predicate)), converter);
    }

    @Override
    public EdgeTraversal hasNot(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.hasNot(propertyKey)), converter);
    }

    @Override
    public EdgeTraversal filter(Predicate<EdgeEntity> predicate) {
        requireNonNull(predicate, "predicat is required");

        Predicate<Traverser<Edge>> p = e -> predicate.test(converter.toEdgeEntity(e.get()));
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.filter(p)), converter);
    }

    @Override
    public EdgeTraversal limit(long limit) {
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.limit(limit)), converter);
    }

    @Override
    public EdgeTraversal range(long start, long end) {
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.range(start, end)), converter);
    }

    @Override
    public EdgeRepeatTraversal repeat() {
        return new DefaultEdgeRepeatTraversal(supplier, flow, converter);
    }


    @Override
    public VertexTraversal inV() {
        return new DefaultVertexTraversal(supplier, flow.andThen(GraphTraversal::inV), converter);
    }

    @Override
    public VertexTraversal outV() {
        return new DefaultVertexTraversal(supplier, flow.andThen(GraphTraversal::outV), converter);
    }

    @Override
    public VertexTraversal bothV() {
        return new DefaultVertexTraversal(supplier, flow.andThen(GraphTraversal::bothV), converter);
    }

    @Override
    public EdgeTraversal dedup(String... labels) {
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.dedup(labels)), converter);
    }


    @Override
    public Optional<EdgeEntity> next() {
        Optional<Edge> edgeOptional = flow.apply(supplier.get()).tryNext();
        if (edgeOptional.isPresent()) {
            Edge edge = edgeOptional.get();
            return Optional.of(converter.toEdgeEntity(edge));

        }
        return Optional.empty();
    }

    @Override
    public Optional<EdgeEntity> getSingleResult() {
        Stream<EdgeEntity> result = getResult();
        final Iterator<EdgeEntity> iterator = result.iterator();

        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final EdgeEntity entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.of(entity);
        }
        throw new NonUniqueResultException("The Edge traversal query returns more than one result");
    }

    @Override
    public Stream<EdgeEntity> getResult() {
        return stream();
    }

    @Override
    public Stream<EdgeEntity> stream() {
        return flow.apply(supplier.get()).toList().stream().map(converter::toEdgeEntity);
    }

    @Override
    public Stream<EdgeEntity> next(int limit) {
        return flow.apply(supplier.get()).next(limit).stream().map(converter::toEdgeEntity);
    }

    @Override
    public ValueMapTraversal valueMap(String... propertyKeys) {
        return new DefaultValueMapTraversal(supplier, flow.andThen(g -> g.valueMap(false, propertyKeys)));
    }

    @Override
    public EdgeTraversalOrder orderBy(String property) {
        requireNonNull(property, "property is required");
        return new DefaultEdgeTraversalOrder(supplier, flow, converter, property);
    }

    @Override
    public long count() {
        return flow.apply(supplier.get()).count().tryNext().orElse(0L);
    }


}
