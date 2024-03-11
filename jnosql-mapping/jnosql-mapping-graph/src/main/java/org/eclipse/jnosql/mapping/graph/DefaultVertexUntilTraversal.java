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

import jakarta.nosql.Entity;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

class DefaultVertexUntilTraversal extends AbstractVertexTraversal implements VertexUntilTraversal {


    DefaultVertexUntilTraversal(Supplier<GraphTraversal<?, ?>> supplier, Function<GraphTraversal<?, ?>,
            GraphTraversal<Vertex, Vertex>> flow, EntityConverter converter) {
        super(supplier, flow, converter);
    }

    @Override
    public VertexTraversal has(String propertyKey, Object value) {

        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(value, "value is required");
        Traversal<?, Vertex> condition = __.has(propertyKey, value);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal has(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        Traversal<?, Vertex> condition = __.has(propertyKey);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal has(String propertyKey, P<?> predicate) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(predicate, "predicate is required");
        Traversal<?, Vertex> condition = __.has(propertyKey, predicate);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal has(T accessor, Object value) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(value, "value is required");
        Traversal<?, Vertex> condition = __.has(accessor, value);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal has(T accessor, P<?> predicate) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(predicate, "predicate is required");
        Traversal<?, Vertex> condition = __.has(accessor, predicate);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal hasNot(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        Traversal<?, Vertex> condition = __.hasNot(propertyKey);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal out(String... labels) {
        Stream.of(labels).forEach(l -> Objects.requireNonNull(l, "label is required"));
        Traversal<?, Vertex> condition = __.out(labels);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal in(String... labels) {
        Stream.of(labels).forEach(l -> Objects.requireNonNull(l, "label is required"));
        Traversal<?, Vertex> condition = __.in(labels);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal both(String... labels) {
        Stream.of(labels).forEach(l -> Objects.requireNonNull(l, "label is required"));
        Traversal<?, Vertex> condition = __.both(labels);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public VertexTraversal hasLabel(String label) {
        requireNonNull(label, "label is required");
        Traversal<?, Vertex> condition = __.hasLabel(label);
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.until(condition)), converter);
    }

    @Override
    public <T> VertexTraversal hasLabel(Class<T> type) {
        requireNonNull(type, "type is required");
        Entity entity = type.getAnnotation(Entity.class);
        String label = Optional.ofNullable(entity).map(Entity::value).orElse(type.getName());
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.hasLabel(label)), converter);
    }

    @Override
    public <T> VertexTraversal hasLabel(P<String> predicate) {
        requireNonNull(predicate, "predicate is required");
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.hasLabel(predicate)), converter);
    }
}
