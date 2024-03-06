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
package org.eclipse.jnosql.communication.graph;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.eclipse.jnosql.communication.semistructured.DeleteQuery;
import org.eclipse.jnosql.communication.semistructured.SelectQuery;

import java.time.Duration;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Default implementation of {@link GraphDatabaseManager} that serves as an adapter to the TinkerPop
 * graph database provided by the Apache TinkerPop framework.
 * <p>
 * This implementation wraps a TinkerPop {@link Graph} instance and provides methods to interact with
 * the underlying graph database, execute graph traversals, and perform other graph-related operations.
 * </p>
 * <p>
 * Note that this implementation does not support certain operations such as insertions with a duration,
 * as indicated by the UnsupportedOperationException thrown by those methods.
 * </p>
 */
public class DefaultGraphDatabaseManager implements GraphDatabaseManager {

    static final String ID_PROPERTY = "_id";
    private final Graph graph;

    private DefaultGraphDatabaseManager(Graph graph) {
        this.graph = graph;
    }

    @Override
    public Graph get() {
        return graph;
    }

    @Override
    public String name() {
        return "The tinkerpop graph database manager";
    }

    @Override
    public CommunicationEntity insert(CommunicationEntity entity) {
        Objects.requireNonNull(entity, "entity is required");
        Vertex vertex = graph.addVertex(entity.name());
        entity.toMap().forEach(vertex::property);
        entity.add(ID_PROPERTY, vertex.id());
        return entity;
    }

    @Override
    public CommunicationEntity insert(CommunicationEntity entity, Duration duration) {
        throw new UnsupportedOperationException("There is no support to insert with duration");
    }

    @Override
    public Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        return null;
    }

    @Override
    public Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> iterable, Duration duration) {
        throw new UnsupportedOperationException("There is no support to insert with duration");
    }

    @Override
    public CommunicationEntity update(CommunicationEntity entity) {
        return null;
    }

    @Override
    public Iterable<CommunicationEntity> update(Iterable<CommunicationEntity> entities) {
        return null;
    }

    @Override
    public void delete(DeleteQuery deleteQuery) {

    }

    @Override
    public Stream<CommunicationEntity> select(SelectQuery selectQuery) {
        return null;
    }

    @Override
    public long count(String s) {
        return 0;
    }

    @Override
    public void close() {

    }

    /**
     * Creates a new instance of DefaultGraphDatabaseManager with the specified TinkerPop Graph.
     *
     * @param graph the TinkerPop Graph instance to be managed
     * @return a new DefaultGraphDatabaseManager instance
     * @throws NullPointerException if the graph parameter is null
     */
    public static GraphDatabaseManager of(Graph graph) {
        Objects.requireNonNull(graph, "graph is required");
        return new DefaultGraphDatabaseManager(graph);
    }
}
