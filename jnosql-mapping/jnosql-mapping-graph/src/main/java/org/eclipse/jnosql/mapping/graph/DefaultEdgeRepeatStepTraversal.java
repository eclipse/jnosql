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

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import java.util.function.Function;
import java.util.function.Supplier;

class DefaultEdgeRepeatStepTraversal extends AbstractEdgeTraversal implements EdgeRepeatStepTraversal {

    DefaultEdgeRepeatStepTraversal(Supplier<GraphTraversal<?, ?>> supplier,
                                   Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Edge>> flow,
                                   EntityConverter converter) {
        super(supplier, flow, converter);
    }

    @Override
    public EdgeTraversal times(int times) {
        return new DefaultEdgeTraversal(supplier, flow.andThen(g -> g.times(times)), converter);
    }

    @Override
    public EdgeUntilTraversal until() {
        return new DefaultEdgeUntilTraversal(supplier, flow, converter);
    }
}
