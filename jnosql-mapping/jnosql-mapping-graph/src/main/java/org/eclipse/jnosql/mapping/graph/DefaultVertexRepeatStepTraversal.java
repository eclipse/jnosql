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
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import java.util.function.Function;
import java.util.function.Supplier;

class DefaultVertexRepeatStepTraversal extends AbstractVertexTraversal implements VertexRepeatStepTraversal {


    DefaultVertexRepeatStepTraversal(Supplier<GraphTraversal<?, ?>> supplier, Function<GraphTraversal<?, ?>,
            GraphTraversal<Vertex, Vertex>> flow, EntityConverter converter) {
        super(supplier, flow, converter);
    }

    @Override
    public VertexTraversal times(int times) {
        return new DefaultVertexTraversal(supplier, flow.andThen(g -> g.times(times)), converter);
    }

    @Override
    public VertexUntilTraversal until() {
        return new DefaultVertexUntilTraversal(supplier, flow, converter);
    }
}
