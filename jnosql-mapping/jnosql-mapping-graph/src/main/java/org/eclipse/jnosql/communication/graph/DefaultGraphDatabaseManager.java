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

import jakarta.data.exceptions.EmptyResultException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.CommunicationException;
import org.eclipse.jnosql.communication.ValueUtil;
import org.eclipse.jnosql.communication.semistructured.CommunicationEntity;
import org.eclipse.jnosql.communication.semistructured.DeleteQuery;
import org.eclipse.jnosql.communication.semistructured.SelectQuery;

import java.time.Duration;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Stream;

import static org.apache.tinkerpop.gremlin.process.traversal.Order.asc;
import static org.apache.tinkerpop.gremlin.process.traversal.Order.desc;

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

    public static final String ID_PROPERTY = "_id";
    private final Graph graph;

    DefaultGraphDatabaseManager(Graph graph) {
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
        entity.elements().forEach(e -> vertex.property(e.name(), ValueUtil.convert(e.value())));
        entity.add(ID_PROPERTY, vertex.id());
        vertex.property(ID_PROPERTY, vertex.id());
        GraphTransactionUtil.transaction(graph);
        return entity;
    }

    @Override
    public CommunicationEntity insert(CommunicationEntity entity, Duration duration) {
        throw new UnsupportedOperationException("There is no support to insert with duration");
    }

    @Override
    public Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        entities.forEach(this::insert);
        return entities;
    }

    @Override
    public Iterable<CommunicationEntity> insert(Iterable<CommunicationEntity> iterable, Duration duration) {
        throw new UnsupportedOperationException("There is no support to insert with duration");
    }

    @Override
    public CommunicationEntity update(CommunicationEntity entity) {
        Objects.requireNonNull(entity, "entity is required");
        entity.find(ID_PROPERTY).ifPresent(id -> {
            Iterator<Vertex> vertices = graph.vertices(id.get());
            if(!vertices.hasNext()) {
                throw new EmptyResultException("The entity does not exist with the id: " + id);
            }
            Vertex vertex = vertices.next();
            entity.elements().forEach(e -> vertex.property(e.name(), ValueUtil.convert(e.value())));
        });
        GraphTransactionUtil.transaction(graph);
        return entity;
    }

    @Override
    public Iterable<CommunicationEntity> update(Iterable<CommunicationEntity> entities) {
        Objects.requireNonNull(entities, "entities is required");
        Stream.of(entities).forEach(this::update);
        return entities;
    }

    @Override
    public void delete(DeleteQuery query) {
        Objects.requireNonNull(query, "delete is required");
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().hasLabel(query.name());
        query.condition().ifPresent(c ->{
            GraphTraversal<Vertex, Vertex> predicate = TraversalExecutor.getPredicate(c);
            traversal.filter(predicate);
        });

       traversal.drop().iterate();
        GraphTransactionUtil.transaction(graph);
    }

    @Override
    public Stream<CommunicationEntity> select(SelectQuery query) {
        Objects.requireNonNull(query, "query is required");
        GraphTraversal<Vertex, Vertex> traversal = graph.traversal().V().hasLabel(query.name());
        query.condition().ifPresent(c ->{
            GraphTraversal<Vertex, Vertex> predicate = TraversalExecutor.getPredicate(c);
            traversal.filter(predicate);
        });

        if(query.limit()> 0) {
            traversal.limit(query.limit());
        } else if(query.skip() > 0) {
            traversal.skip(query.skip());
        }
       query.sorts().forEach(
               s -> {
                   if (s.isAscending()) {
                       traversal.order().by(s.property(), asc);
                   } else {
                       traversal.order().by(s.property(), desc);
                   }
               });
        return traversal.toStream().map(CommunicationEntityConverter.INSTANCE);
    }

    @Override
    public long count(String entity) {
        Objects.requireNonNull(entity, "entity is required");
        GraphTraversal<Vertex, Long> count = graph.traversal().V().hasLabel(entity).count();
        return count.next();
    }

    @Override
    public void close() {
        try {
            graph.close();
        } catch (Exception e) {
            throw new CommunicationException("There is an issue when close the Graph connection", e);
        }
    }
}
