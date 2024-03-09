/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.communication.graph.GraphDatabaseManager;

import java.util.function.Supplier;

/**
 * A supplier for obtaining instances of {@link GraphDatabaseManager}.
 * This supplier is {@link ApplicationScoped}, meaning that there will be a single instance
 * per application context.
 */
@ApplicationScoped
class GraphDatabaseManagerSupplier implements Supplier<GraphDatabaseManager> {

    private final Instance<Graph> graphs;

    @Inject
    GraphDatabaseManagerSupplier(Instance<Graph> graphs) {
        this.graphs = graphs;
    }

    GraphDatabaseManagerSupplier() {
        this(null);
    }

    @Override
    @Produces
    public GraphDatabaseManager get() {
        var graph = graphs.get();
        return GraphDatabaseManager.of(graph);
    }
}
