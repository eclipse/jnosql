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
import jakarta.nosql.PreparedStatement;
import jakarta.nosql.QueryMapper;
import org.eclipse.jnosql.mapping.Converters;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.FieldMetadata;
import org.eclipse.jnosql.mapping.util.ConverterUtil;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.structure.T.id;

public abstract class AbstractGraphTemplate implements GraphTemplate {
    private static final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Vertex>> INITIAL_VERTEX =
            g -> (GraphTraversal<Vertex, Vertex>) g;

    private static final Function<GraphTraversal<?, ?>, GraphTraversal<Vertex, Edge>> INITIAL_EDGE =
            g -> (GraphTraversal<Vertex, Edge>) g;


    protected abstract Graph getGraph();

    protected abstract EntitiesMetadata getEntities();

    protected abstract GraphConverter getConverter();

    protected abstract Converters getConverters();

    protected abstract GraphEventPersistManager getEventManager();

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
        UnaryOperator<Vertex> save = v -> {
            GraphTransactionUtil.transaction(getGraph());
            return v;
        };

        return persist(entity, save);
    }

    @Override
    public <T> T insert(T entity, Duration ttl) {
        throw new UnsupportedOperationException("GraphTemplate does not support insert with TTL");
    }

    @Override
    public <T> Iterable<T> insert(Iterable<T> entities, Duration ttl) {
        throw new UnsupportedOperationException("GraphTemplate does not support insert with TTL");
    }

    @Override
    public <T> T update(T entity) {
        requireNonNull(entity, "entity is required");
        checkId(entity);
        if (isIdNull(entity)) {
            throw new IllegalStateException("to update a graph id cannot be null");
        }
        vertex(entity).orElseThrow(() -> new EmptyResultException("Entity does not find in the update"));

        UnaryOperator<Vertex> update = e -> {
            final Vertex vertex = getConverter().toVertex(entity);
            GraphTransactionUtil.transaction(getGraph());
            return vertex;
        };

        return persist(entity, update);
    }

    @Override
    public <T, K> Optional<T> find(Class<T> type, K id) {
        requireNonNull(type, "type is required");
        requireNonNull(id, "id is required");
        EntityMetadata entityMetadata = getEntities().get(type);
        FieldMetadata idField = entityMetadata.id()
                .orElseThrow(() -> IdNotFoundException.newInstance(type));

        Object value = ConverterUtil.getValue(id, entityMetadata, idField.fieldName(), getConverters());

        final Optional<Vertex> vertex = traversal().V(value).hasLabel(entityMetadata.name()).tryNext();
        return vertex.map(getConverter()::toEntity);
    }

    @Override
    public <T> void delete(T idValue) {
        requireNonNull(idValue, "id is required");
        traversal().V(idValue).toStream().forEach(Vertex::remove);
    }

    @Override
    public <T, K> void delete(Class<T> type, K id) {
        requireNonNull(type, "type is required");
        requireNonNull(id, "id is required");
        EntityMetadata mapping = getEntities().get(type);
        traversal()
                .V(id)
                .hasLabel(mapping.name())
                .toStream()
                .forEach(Vertex::remove);
    }

    @Override
    public <T> void deleteEdge(T idEdge) {
        requireNonNull(idEdge, "idEdge is required");
        traversal().E(idEdge).toStream().forEach(Edge::remove);
    }

    @Override
    public <T, K> Optional<T> find(K idValue) {
        requireNonNull(idValue, "id is required");
        Optional<Vertex> vertex = traversal().V(idValue).tryNext();
        return vertex.map(getConverter()::toEntity);
    }

    @Override
    public <T> Iterable<T> insert(Iterable<T> entities) {
        requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::insert).collect(Collectors.toList());
    }

    @Override
    public <T> Iterable<T> update(Iterable<T> entities) {
        requireNonNull(entities, "entities is required");
        return StreamSupport.stream(entities.spliterator(), false)
                .map(this::update).collect(Collectors.toList());
    }

    @Override
    public <T> void delete(Iterable<T> ids) {
        requireNonNull(ids, "ids is required");
        final Object[] vertexIds = StreamSupport.stream(ids.spliterator(), false).toArray(Object[]::new);
        traversal().V(vertexIds).toStream().forEach(Vertex::remove);
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

        Vertex outVertex = vertex(outgoing).orElseThrow(() -> new  EmptyResultException("Outgoing entity does not found"));
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

    private Edge getEdge(String label, Vertex outVertex, Vertex inVertex) {
        final Edge edge = outVertex.addEdge(label, inVertex);
        GraphTransactionUtil.transaction(getGraph());
        return edge;
    }

    @Override
    public <E> Optional<EdgeEntity> edge(E edgeId) {
        requireNonNull(edgeId, "edgeId is required");

        Optional<Edge> edgeOptional = traversal().E(edgeId).tryNext();

        if (edgeOptional.isPresent()) {
            Edge edge = edgeOptional.get();
            return Optional.of(getConverter().toEdgeEntity(edge));
        }

        return Optional.empty();
    }

    @Override
    public <T> Collection<EdgeEntity> edges(T entity, Direction direction) {
        return edgesImpl(entity, direction);
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
    public <K> Collection<EdgeEntity> edgesById(K id, Direction direction, String... labels) {
        return edgesByIdImpl(id, direction, labels);
    }

    @Override
    public <K> Collection<EdgeEntity> edgesById(K id, Direction direction) {
        return edgesByIdImpl(id, direction);
    }

    @SafeVarargs
    @Override
    public final <K> Collection<EdgeEntity> edgesById(K id, Direction direction, Supplier<String>... labels) {
        checkLabelsSupplier(labels);
        return edgesByIdImpl(id, direction, Stream.of(labels).map(Supplier::get).toArray(String[]::new));
    }

    @Override
    public VertexTraversal traversalVertex(Object... vertexIds) {
        if (Stream.of(vertexIds).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("No one vertexId element cannot be null");
        }
        return new DefaultVertexTraversal(() -> traversal().V(vertexIds), INITIAL_VERTEX, getConverter());
    }

    @Override
    public EdgeTraversal traversalEdge(Object... edgeIds) {
        if (Stream.of(edgeIds).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("No one edgeId element cannot be null");
        }
        return new DefaultEdgeTraversal(() -> traversal().E(edgeIds), INITIAL_EDGE, getConverter());
    }

    @Override
    public Transaction transaction() {
        return getGraph().tx();
    }


    @Override
    public <T> Stream<T> query(String gremlin) {
        requireNonNull(gremlin, "query is required");
        return getExecutor().executeGremlin(traversal(), gremlin);
    }

    @Override
    public <T> Optional<T> singleResult(String gremlin) {
        Stream<T> entities = query(gremlin);
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
    public PreparedStatement prepare(String gremlin) {
        requireNonNull(gremlin, "query is required");
        return new DefaultPreparedStatement(getExecutor(), gremlin, traversal());
    }

    protected GraphTraversalSource traversal() {
        return getGraph().traversal();
    }

    protected Iterator<Vertex> vertices(Object id) {
        return getGraph().vertices(id);
    }

    @Override
    public long count(String label) {
        Objects.requireNonNull(label, "label is required");
        return traversal().V().hasLabel(label).count().tryNext().orElse(0L);
    }


    @Override
    public <T> long count(Class<T> type) {
        Objects.requireNonNull(type, "entity class is required");
        return count(getEntities().get(type).name());
    }

    @Override
    public <T> QueryMapper.MapperFrom select(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        GraphTraversal<Vertex, Vertex> traversal = traversal().V().hasLabel(metadata.name());
        return new GraphMapperSelect(metadata,getConverters(), traversal, getConverter());
    }

    @Override
    public <T> QueryMapper.MapperDeleteFrom delete(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        GraphTraversal<Vertex, Vertex> traversal = traversal().V().hasLabel(metadata.name());
        return new GraphMapperDelete(metadata,getConverters(), traversal, getConverter());
    }

    @Override
    public <T> Stream<T> findAll(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        return traversal().V().hasLabel(metadata.name())
                .toStream().map(getConverter()::toEntity);
    }

    @Override
    public <T> void deleteAll(Class<T> type) {
        Objects.requireNonNull(type, "type is required");
        EntityMetadata metadata = getEntities().get(type);
        traversal().V().hasLabel(metadata.name()).toStream().forEach(Vertex::remove);
    }

    private <K> Collection<EdgeEntity> edgesByIdImpl(K id, Direction direction, String... labels) {

        requireNonNull(id, "id is required");
        requireNonNull(direction, "direction is required");

        Iterator<Vertex> vertices = vertices(id);
        if (vertices.hasNext()) {
            List<Edge> edges = new ArrayList<>();
            vertices.next().edges(direction, labels).forEachRemaining(edges::add);
            return edges.stream().map(getConverter()::toEdgeEntity).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private <T> Optional<Vertex> vertex(T entity) {
        EntityMetadata entityMetadata = getEntities().get(entity.getClass());
        FieldMetadata field = entityMetadata.id().get();
        Object id = field.read(entity);
        Iterator<Vertex> vertices = vertices(id);
        if (vertices.hasNext()) {
            return Optional.of(vertices.next());
        }
        return Optional.empty();
    }

    private <T> Collection<EdgeEntity> edgesImpl(T entity, Direction direction, String... labels) {
        requireNonNull(entity, "entity is required");

        if (isIdNull(entity)) {
            throw new IllegalStateException("Entity id is required");
        }

        if (vertex(entity).isEmpty()) {
            return Collections.emptyList();
        }
        Object id = getConverter().toVertex(entity).id();
        return edgesByIdImpl(id, direction, labels);
    }

    private void checkLabelsSupplier(Supplier<String>[] labels) {
        if (Stream.of(labels).anyMatch(Objects::isNull)) {
            throw new IllegalStateException("Item cannot be null");
        }
    }

    private <T> boolean isIdNull(T entity) {
        EntityMetadata entityMetadata = getEntities().get(entity.getClass());
        FieldMetadata field = entityMetadata.id().get();
        return isNull(field.read(entity));

    }

    private <T> void checkId(T entity) {
        EntityMetadata entityMetadata = getEntities().get(entity.getClass());
        entityMetadata.id().orElseThrow(() -> IdNotFoundException.newInstance(entity.getClass()));
    }

    protected <T> T persist(T entity, UnaryOperator<Vertex> persistAction) {
        return Stream.of(entity)
                .map(toUnary(getEventManager()::firePreEntity))
                .map(getConverter()::toVertex)
                .map(persistAction)
                .map(t -> getConverter().toEntity(entity, t))
                .map(toUnary(getEventManager()::firePostEntity))
                .findFirst()
                .orElseThrow();
    }

    private <T> UnaryOperator<T> toUnary(Consumer<T> consumer) {
        return t -> {
            consumer.accept(t);
            return t;
        };
    }
}
