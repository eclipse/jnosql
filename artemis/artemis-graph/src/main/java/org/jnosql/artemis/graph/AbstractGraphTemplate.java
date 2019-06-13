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
package org.jnosql.artemis.graph;

import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.jnosql.artemis.EntityNotFoundException;
import jakarta.nosql.mapping.IdNotFoundException;
import jakarta.nosql.mapping.PreparedStatement;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;
import jakarta.nosql.mapping.reflection.FieldMapping;
import static jakarta.nosql.NonUniqueResultException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.structure.T.id;

public abstract class AbstractGraphTemplate implements GraphTemplate {
    private static final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Vertex>> INITIAL_VERTEX =
            g -> (GraphTraversal<Vertex, Vertex>) g;

    private static final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Edge>> INITIAL_EDGE =
            g -> (GraphTraversal<Vertex, Edge>) g;


    protected abstract Graph getGraph();

    protected abstract ClassMappings getClassMappings();

    protected abstract GraphConverter getConverter();

    protected abstract GraphWorkflow getFlow();

    private GremlinExecutor gremlinExecutor;

    private GremlinExecutor getExecutor() {
        if (Objects.isNull(gremlinExecutor)) {
            this.gremlinExecutor = new GremlinExecutor(getConverter());
        }
        return gremlinExecutor;
    }

    @Override
    public <T> T insert(T entity) {
        requireNonNull(entity, "entity is required");
        checkId(entity);
        UnaryOperator<Vertex> save = v -> v;

        return getFlow().flow(entity, save);
    }

    @Override
    public <T> T update(T entity) {
        requireNonNull(entity, "entity is required");
        checkId(entity);
        if (isIdNull(entity)) {
            throw new IllegalStateException("to update a graph id cannot be null");
        }
        getVertex(entity).orElseThrow(() -> new EntityNotFoundException("Entity does not find in the update"));

        UnaryOperator<Vertex> update = e -> getConverter().toVertex(entity);
        return getFlow().flow(entity, update);
    }

    @Override
    public <T> void delete(T idValue) {
        requireNonNull(idValue, "id is required");
        List<Vertex> vertices = getTraversal().V(idValue).toList();
        vertices.forEach(Vertex::remove);

    }

    @Override
    public <T> void deleteEdge(T idEdge) {
        requireNonNull(idEdge, "idEdge is required");
        List<Edge> edges = getTraversal().E(idEdge).toList();
        edges.forEach(Edge::remove);
    }

    @Override
    public <T, K> Optional<T> find(K idValue) {
        requireNonNull(idValue, "id is required");
        Optional<Vertex> vertex = getTraversal().V(idValue).tryNext();
        return vertex.map(getConverter()::toEntity);
    }

    @Override
    public <O, I> EdgeEntity edge(O outgoing, String label, I incoming) {

        requireNonNull(incoming, "incoming is required");
        requireNonNull(label, "label is required");
        requireNonNull(outgoing, "outgoing is required");

        checkId(outgoing);
        checkId(incoming);

        if (isIdNull(outgoing)) {
            throw new IllegalStateException("outgoing Id field is required");
        }

        if (isIdNull(incoming)) {
            throw new IllegalStateException("incoming Id field is required");
        }


        Vertex outVertex = getVertex(outgoing).orElseThrow(() -> new EntityNotFoundException("Outgoing entity does not found"));
        Vertex inVertex = getVertex(incoming).orElseThrow(() -> new EntityNotFoundException("Incoming entity does not found"));

        final Predicate<Traverser<Edge>> predicate = t -> {
            Edge e = t.get();
            return e.inVertex().id().equals(inVertex.id())
                    && e.outVertex().id().equals(outVertex.id());
        };

        Optional<Edge> edge = getTraversal().V(outVertex.id())
                .out(label).has(id, inVertex.id()).inE(label).filter(predicate).tryNext();

        return edge.<EdgeEntity>map(edge1 -> new DefaultEdgeEntity<>(edge1, incoming, outgoing))
                .orElseGet(() -> new DefaultEdgeEntity<>(outVertex.addEdge(label, inVertex), incoming, outgoing));


    }

    @Override
    public <E> Optional<EdgeEntity> edge(E edgeId) {
        requireNonNull(edgeId, "edgeId is required");

        Optional<Edge> edgeOptional = getTraversal().E(edgeId).tryNext();

        if (edgeOptional.isPresent()) {
            Edge edge = edgeOptional.get();
            return Optional.of(getConverter().toEdgeEntity(edge));
        }

        return Optional.empty();
    }


    @Override
    public <T> Collection<EdgeEntity> getEdges(T entity, Direction direction) {
        return getEdgesImpl(entity, direction);
    }

    @Override
    public <T> Collection<EdgeEntity> getEdges(T entity, Direction direction, String... labels) {
        return getEdgesImpl(entity, direction, labels);
    }


    @SafeVarargs
    @Override
    public final <T> Collection<EdgeEntity> getEdges(T entity, Direction direction, Supplier<String>... labels) {
        checkLabelsSupplier(labels);
        return getEdgesImpl(entity, direction, Stream.of(labels).map(Supplier::get).toArray(String[]::new));
    }


    @Override
    public <K> Collection<EdgeEntity> getEdgesById(K id, Direction direction, String... labels) {
        return getEdgesByIdImpl(id, direction, labels);
    }

    @Override
    public <K> Collection<EdgeEntity> getEdgesById(K id, Direction direction) {
        return getEdgesByIdImpl(id, direction);
    }

    @SafeVarargs
    @Override
    public final <K> Collection<EdgeEntity> getEdgesById(K id, Direction direction, Supplier<String>... labels) {
        checkLabelsSupplier(labels);
        return getEdgesByIdImpl(id, direction, Stream.of(labels).map(Supplier::get).toArray(String[]::new));
    }


    @Override
    public VertexTraversal getTraversalVertex(Object... vertexIds) {
        if (Stream.of(vertexIds).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("No one vertexId element cannot be null");
        }
        return new DefaultVertexTraversal(() -> getTraversal().V(vertexIds), INITIAL_VERTEX, getConverter());
    }

    @Override
    public EdgeTraversal getTraversalEdge(Object... edgeIds) {
        if (Stream.of(edgeIds).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("No one edgeId element cannot be null");
        }
        return new DefaultEdgeTraversal(() -> getTraversal().E(edgeIds), INITIAL_EDGE, getConverter());
    }

    @Override
    public Transaction getTransaction() {
        return getGraph().tx();
    }


    @Override
    public <T> List<T> query(String gremlin) {
        requireNonNull(gremlin, "query is required");
        return getExecutor().executeGremlin(getTraversal(), gremlin);
    }

    @Override
    public <T> Optional<T> singleResult(String gremlin) {
        List<T> entities = query(gremlin);
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        if (entities.size() == 1) {
            return Optional.ofNullable(entities.get(0));
        }
        throw new NonUniqueResultException("The gremlin query returns more than one result: " + gremlin);

    }

    @Override
    public PreparedStatement prepare(String gremlin) {
        requireNonNull(gremlin, "query is required");
        return new DefaultPreparedStatement(getExecutor(), gremlin, getTraversal());
    }

    protected GraphTraversalSource getTraversal() {
        return getGraph().traversal();
    }

    protected Iterator<Vertex> getVertices(Object id) {
        return getGraph().vertices(id);
    }


    @Override
    public long count(String label) {
        Objects.requireNonNull(label, "label is required");
        return getTraversal().V().hasLabel(label).count().tryNext().orElse(0L);
    }


    @Override
    public <T> long count(Class<T> entityClass) {
        Objects.requireNonNull(entityClass, "entity class is required");
        return count(getClassMappings().get(entityClass).getName());
    }


    private <K> Collection<EdgeEntity> getEdgesByIdImpl(K id, Direction direction, String... labels) {

        requireNonNull(id, "id is required");
        requireNonNull(direction, "direction is required");

        Iterator<Vertex> vertices = getVertices(id);
        if (vertices.hasNext()) {
            List<Edge> edges = new ArrayList<>();
            vertices.next().edges(direction, labels).forEachRemaining(edges::add);
            return edges.stream().map(getConverter()::toEdgeEntity).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private <T> Optional<Vertex> getVertex(T entity) {
        ClassMapping classMapping = getClassMappings().get(entity.getClass());
        FieldMapping field = classMapping.getId().get();
        Object id = field.read(entity);
        Iterator<Vertex> vertices = getVertices(id);
        if (vertices.hasNext()) {
            return Optional.of(vertices.next());
        }
        return Optional.empty();
    }


    private <T> Collection<EdgeEntity> getEdgesImpl(T entity, Direction direction, String... labels) {
        requireNonNull(entity, "entity is required");

        if (isIdNull(entity)) {
            throw new IllegalStateException("Entity id is required");
        }

        if (!getVertex(entity).isPresent()) {
            return Collections.emptyList();
        }
        Object id = getConverter().toVertex(entity).id();
        return getEdgesByIdImpl(id, direction, labels);
    }

    private void checkLabelsSupplier(Supplier<String>[] labels) {
        if (Stream.of(labels).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("Item cannot be null");
        }
    }

    private <T> boolean isIdNull(T entity) {
        ClassMapping classMapping = getClassMappings().get(entity.getClass());
        FieldMapping field = classMapping.getId().get();
        return isNull(field.read(entity));

    }


    private <T> void checkId(T entity) {
        ClassMapping classMapping = getClassMappings().get(entity.getClass());
        classMapping.getId().orElseThrow(() -> IdNotFoundException.newInstance(entity.getClass()));
    }
}
