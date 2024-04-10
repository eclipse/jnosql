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

import jakarta.data.exceptions.EmptyResultException;
import jakarta.data.exceptions.NonUniqueResultException;
import org.eclipse.jnosql.mapping.PreparedStatement;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.graph.CommunicationEntityConverter;
import org.eclipse.jnosql.communication.graph.GraphDatabaseManager;
import org.eclipse.jnosql.communication.graph.GraphTransactionUtil;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.semistructured.AbstractSemiStructuredTemplate;

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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.structure.T.id;

abstract class AbstractGraphTemplate extends AbstractSemiStructuredTemplate implements GraphTemplate {

    private static final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Vertex>> INITIAL_VERTEX =
            g -> (GraphTraversal<Vertex, Vertex>) g;

    private static final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Edge>> INITIAL_EDGE =
            g -> (GraphTraversal<Vertex, Edge>) g;


    /**
     * Retrieves the {@link GraphDatabaseManager} associated with this graph template.
     *
     * @return the {@link GraphDatabaseManager} associated with this graph template
     */
    protected abstract GraphDatabaseManager manager();

    /**
     * Retrieves the {@link GraphTraversalSource} associated with this graph template.
     *
     * @return the {@link GraphTraversalSource} associated with this graph template
     */
    protected abstract GraphTraversalSource traversal();

    /**
     * Retrieves the {@link Graph} associated with this graph template.
     *
     * @return the {@link Graph} associated with this graph template
     */
    protected abstract Graph graph();

    private GremlinExecutor gremlinExecutor;


    @Override
    public <T, K> Optional<T> find(K idValue) {
        requireNonNull(idValue, "id is required");
        Optional<Vertex> vertex = traversal().V(idValue).tryNext();
        return vertex.map(v -> converter().toEntity(CommunicationEntityConverter.INSTANCE.apply(v)));
    }

    @Override
    public <T> void delete(T id) {
        requireNonNull(id, "id is required");
        traversal().V(id).toStream().forEach(Vertex::remove);
    }

    @Override
    public <T> void deleteEdge(T id) {
        requireNonNull(id, "id is required");
        traversal().E(id).toStream().forEach(Edge::remove);
    }

    @Override
    public <T> void deleteEdge(Iterable<T> ids) {
        requireNonNull(ids, "ids is required");
        final Object[] edgeIds = StreamSupport.stream(ids.spliterator(), false).toArray(Object[]::new);
        traversal().E(edgeIds).toStream().forEach(Edge::remove);
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

        Vertex outVertex = vertex(outgoing).orElseThrow(() -> new EmptyResultException("Outgoing entity does not found"));
        Vertex inVertex = vertex(incoming).orElseThrow(() -> new  EmptyResultException("Incoming entity does not found"));

        final Predicate<Traverser<Edge>> predicate = t -> {
            Edge e = t.get();
            return e.inVertex().id().equals(inVertex.id())
                    && e.outVertex().id().equals(outVertex.id());
        };

        Optional<Edge> edge = traversal().V(outVertex.id())
                .out(label).has(id, inVertex.id()).inE(label).filter(predicate).tryNext();

        return edge.<EdgeEntity>map(edge1 -> new DefaultEdgeEntity<>(edge1, incoming, outgoing))
                .orElseGet(() -> new DefaultEdgeEntity<>(getEdge(label, outVertex, inVertex), incoming, outgoing));
    }

    @Override
    public <K> Collection<EdgeEntity> edgesById(K id, Direction direction, String... labels) {
        requireNonNull(id, "id is required");
        requireNonNull(direction, "direction is required");

        Iterator<Vertex> vertices = vertices(id);
        if (vertices.hasNext()) {
            List<Edge> edges = new ArrayList<>();
            vertices.next().edges(direction, labels).forEachRemaining(edges::add);
            return edges.stream().map(e ->EdgeEntity.of(converter(), e)).toList();
        }
        return Collections.emptyList();
    }

    @SafeVarargs
    @Override
    public final <K> Collection<EdgeEntity> edgesById(K id, Direction direction, Supplier<String>... labels) {
        checkLabelsSupplier(labels);
        return edgesByIdImpl(id, direction, Stream.of(labels).map(Supplier::get).toArray(String[]::new));
    }

    @Override
    public <K> Collection<EdgeEntity> edgesById(K id, Direction direction) {
        return edgesByIdImpl(id, direction);
    }

    @Override
    public <T> Collection<EdgeEntity> edges(T entity, Direction direction, String... labels) {
        return edgesImpl(entity, direction, labels);
    }

    @SafeVarargs
    @Override
    public final <T> Collection<EdgeEntity> edges(T entity, Direction direction, Supplier<String>... labels) {
        checkLabelsSupplier(labels);
        return edgesImpl(entity, direction, Stream.of(labels).map(Supplier::get).toArray(String[]::new));
    }

    @Override
    public <T> Collection<EdgeEntity> edges(T entity, Direction direction) {
        return edgesImpl(entity, direction);
    }

    @Override
    public <E> Optional<EdgeEntity> edge(E edgeId) {
        requireNonNull(edgeId, "edgeId is required");

        Optional<Edge> edgeOptional = traversal().E(edgeId).tryNext();

        if (edgeOptional.isPresent()) {
            Edge edge = edgeOptional.get();
            return Optional.of(EdgeEntity.of(converter(), edge));
        }

        return Optional.empty();
    }

    @Override
    public VertexTraversal traversalVertex(Object... vertexIds) {
        if (Stream.of(vertexIds).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("No one vertexId element cannot be null");
        }
        return new DefaultVertexTraversal(() -> traversal().V(vertexIds), INITIAL_VERTEX, converter());
    }

    @Override
    public EdgeTraversal traversalEdge(Object... edgeIds) {
        if (Stream.of(edgeIds).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("No one edgeId element cannot be null");
        }
        return new DefaultEdgeTraversal(() -> traversal().E(edgeIds), INITIAL_EDGE, converter());
    }

    @Override
    public Transaction transaction() {
        return graph().tx();
    }

    @Override
    public <T> Stream<T> gremlin(String gremlin) {
        requireNonNull(gremlin, "gremlin is required");
        return executor().executeGremlin(traversal(), gremlin);
    }

    @Override
    public <T> Optional<T> gremlinSingleResult(String gremlin) {
        Stream<T> entities = gremlin(gremlin);
        final Iterator<T> iterator = entities.iterator();
        if (!iterator.hasNext()) {
            return Optional.empty();
        }
        final T entity = iterator.next();
        if (!iterator.hasNext()) {
            return Optional.ofNullable(entity);
        }
        throw new NonUniqueResultException("The gremlin query returns more than one result: " + gremlin);
    }

    @Override
    public PreparedStatement gremlinPrepare(String gremlin){
        requireNonNull(gremlin, "query is required");
        return new DefaultPreparedStatement(executor(), gremlin, traversal());
    }

    private <T> void checkId(T entity) {
        EntityMetadata entityMetadata = entities().get(entity.getClass());
        entityMetadata.id().orElseThrow(() -> IdNotFoundException.newInstance(entity.getClass()));
    }

    private <T> boolean isIdNull(T entity) {
        EntityMetadata entityMetadata = entities().get(entity.getClass());
        FieldMetadata field = entityMetadata.id().orElseThrow(() -> IdNotFoundException.newInstance(entity.getClass()));
        return isNull(field.read(entity));

    }

    private <T> Optional<Vertex> vertex(T entity) {
        EntityMetadata entityMetadata = entities().get(entity.getClass());
        FieldMetadata field = entityMetadata.id().orElseThrow(() -> IdNotFoundException.newInstance(entity.getClass()));
        Object id = field.read(entity);
        Iterator<Vertex> vertices = vertices(id);
        if (vertices.hasNext()) {
            return Optional.of(vertices.next());
        }
        return Optional.empty();
    }

    protected Iterator<Vertex> vertices(Object id) {
        return graph().vertices(id);
    }

    private Edge getEdge(String label, Vertex outVertex, Vertex inVertex) {
        final Edge edge = outVertex.addEdge(label, inVertex);
        GraphTransactionUtil.transaction(graph());
        return edge;
    }

    private void checkLabelsSupplier(Supplier<String>[] labels) {
        if (Stream.of(labels).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("Item cannot be null");
        }
    }

    private <K> Collection<EdgeEntity> edgesByIdImpl(K id, Direction direction, String... labels) {

        requireNonNull(id, "id is required");
        requireNonNull(direction, "direction is required");

        Iterator<Vertex> vertices = vertices(id);
        if (vertices.hasNext()) {
            List<Edge> edges = new ArrayList<>();
            vertices.next().edges(direction, labels).forEachRemaining(edges::add);
            return edges.stream().map(e -> EdgeEntity.of(converter(), e)).toList();
        }
        return Collections.emptyList();
    }

    private <T> Collection<EdgeEntity> edgesImpl(T entity, Direction direction, String... labels) {
        requireNonNull(entity, "entity is required");

        if (isIdNull(entity)) {
            throw new IllegalStateException("Entity id is required");
        }

        Optional<Vertex> vertex = vertex(entity);
        if (vertex.isEmpty()) {
            return Collections.emptyList();
        }

        Object id = vertex.orElseThrow().id();
        return edgesByIdImpl(id, direction, labels);
    }

    private GremlinExecutor executor() {
        if (Objects.isNull(gremlinExecutor)) {
            this.gremlinExecutor = new GremlinExecutor(converter());
        }
        return gremlinExecutor;
    }

}
