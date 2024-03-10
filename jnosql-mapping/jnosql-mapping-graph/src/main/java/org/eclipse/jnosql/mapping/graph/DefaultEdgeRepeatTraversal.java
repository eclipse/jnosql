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

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

class DefaultEdgeRepeatTraversal extends AbstractEdgeTraversal implements EdgeRepeatTraversal {


    DefaultEdgeRepeatTraversal(Supplier<GraphTraversal<?, ?>> supplier,
                               Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Edge>> flow,
                               EntityConverter converter) {
        super(supplier, flow, converter);
    }

    @Override
    public EdgeRepeatStepTraversal has(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");

        Traversal<?, Edge> condition = __.has(propertyKey);
        return new DefaultEdgeRepeatStepTraversal(supplier, flow.andThen(g -> g.repeat(condition)), converter);
    }

    @Override
    public EdgeRepeatStepTraversal has(String propertyKey, Object value) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(value, "value is required");
        Traversal<?, Edge> condition = __.has(propertyKey, value);
        return new DefaultEdgeRepeatStepTraversal(supplier, flow.andThen(g -> g.repeat(condition)), converter);
    }

    @Override
    public EdgeRepeatStepTraversal has(String propertyKey, P<?> predicate) {
        requireNonNull(propertyKey, "propertyKey is required");
        requireNonNull(predicate, "predicate is required");
        Traversal<?, Edge> condition = __.has(propertyKey, predicate);
        return new DefaultEdgeRepeatStepTraversal(supplier, flow.andThen(g -> g.repeat(condition)), converter);
    }

    @Override
    public EdgeRepeatStepTraversal has(T accessor, Object value) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(value, "value is required");
        Traversal<?, Edge> condition = __.has(accessor, value);
        return new DefaultEdgeRepeatStepTraversal(supplier, flow.andThen(g -> g.repeat(condition)), converter);
    }

    @Override
    public EdgeRepeatStepTraversal has(T accessor, P<?> predicate) {
        requireNonNull(accessor, "accessor is required");
        requireNonNull(predicate, "predicate is required");
        Traversal<?, Edge> condition = __.has(accessor, predicate);
        return new DefaultEdgeRepeatStepTraversal(supplier, flow.andThen(g -> g.repeat(condition)), converter);
    }

    @Override
    public EdgeRepeatStepTraversal hasNot(String propertyKey) {
        requireNonNull(propertyKey, "propertyKey is required");
        Traversal<?, Edge> condition = __.hasNot(propertyKey);
        return new DefaultEdgeRepeatStepTraversal(supplier, flow.andThen(g -> g.repeat(condition)), converter);
    }
}


