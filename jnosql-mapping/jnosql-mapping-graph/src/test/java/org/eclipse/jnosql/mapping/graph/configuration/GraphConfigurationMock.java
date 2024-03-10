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
package org.eclipse.jnosql.mapping.graph.configuration;

import org.apache.commons.configuration2.Configuration;
import org.apache.tinkerpop.gremlin.process.computer.GraphComputer;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.communication.graph.GraphConfiguration;

import java.util.Iterator;

class GraphConfigurationMock implements GraphConfiguration {

    @Override
    public Graph apply(Settings settings) {
        return new GraphMock(settings);
    }

    public record GraphMock(Settings settings) implements Graph {

        @Override
            public Vertex addVertex(Object... keyValues) {
                return null;
            }

            @Override
            public <C extends GraphComputer> C compute(Class<C> graphComputerClass) throws IllegalArgumentException {
                return null;
            }

            @Override
            public GraphComputer compute() throws IllegalArgumentException {
                return null;
            }

            @Override
            public Iterator<Vertex> vertices(Object... vertexIds) {
                return null;
            }

            @Override
            public Iterator<Edge> edges(Object... edgeIds) {
                return null;
            }

            @Override
            public Transaction tx() {
                return null;
            }

            @Override
            public void close() throws Exception {

            }

            @Override
            public Variables variables() {
                return null;
            }

            @Override
            public Configuration configuration() {
                return null;
            }

        }
}
