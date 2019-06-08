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
package org.jnosql.artemis.graph.cdi;

import org.apache.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.jnosql.artemis.Database;
import org.jnosql.artemis.DatabaseType;
import org.jnosql.artemis.graph.GraphTraversalSourceSupplier;
import org.mockito.Mockito;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import java.io.File;
import java.util.Collections;

import static java.util.Collections.singleton;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ApplicationScoped
public class GraphProducer {


    private Graph graph = Neo4jGraph.open(new File("").getAbsolutePath() + "/target/jnosql-graph");

    @Produces
    @ApplicationScoped
    public Graph getGraph() {
        return graph;
    }

    @Produces
    @ApplicationScoped
    public GraphTraversalSourceSupplier getGraphTraversalSource() {
        return () -> graph.traversal();
    }

    @Produces
    @ApplicationScoped
    @Database(value = DatabaseType.GRAPH, provider = "graphRepositoryMock")
    public Graph getGraphMock() {

        Graph graphMock = mock(Graph.class);

        Vertex vertex = mock(Vertex.class);

        when(vertex.label()).thenReturn("Person");
        when(vertex.id()).thenReturn(10L);
        when(graphMock.vertices(10L)).thenReturn(Collections.<Vertex>emptyList().iterator());
        when(vertex.keys()).thenReturn(singleton("name"));
        when(vertex.value("name")).thenReturn("nameMock");
        when(graphMock.addVertex(Mockito.anyString())).thenReturn(vertex);

        return graphMock;
    }

    public void dispose(@Disposes Graph graph) throws Exception {
        graph.close();
    }
}
