package org.eclipse.jnosql.mapping.graph;

import jakarta.data.exceptions.EmptyResultException;
import org.apache.tinkerpop.gremlin.process.traversal.Traverser;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.eclipse.jnosql.communication.graph.GraphDatabaseManager;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.FieldMetadata;
import org.eclipse.jnosql.mapping.semistructured.AbstractSemistructuredTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static org.apache.tinkerpop.gremlin.structure.T.id;

abstract class AbstractGraphTemplate extends AbstractSemistructuredTemplate implements GraphTemplate {

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

    @Override
    public <K> Collection<EdgeEntity> edgesById(K id, Direction direction, Supplier<String>... labels) {
        return null;
    }

    @Override
    public <K> Collection<EdgeEntity> edgesById(K id, Direction direction) {
        return null;
    }

    @Override
    public <T> Collection<EdgeEntity> edges(T entity, Direction direction, String... labels) {
        return null;
    }

    @Override
    public <T> Collection<EdgeEntity> edges(T entity, Direction direction, Supplier<String>... labels) {
        return null;
    }

    @Override
    public <T> Collection<EdgeEntity> edges(T entity, Direction direction) {
        return null;
    }

    @Override
    public <E> Optional<EdgeEntity> edge(E edgeId) {
        return Optional.empty();
    }

    @Override
    public VertexTraversal traversalVertex(Object... vertexIds) {
        return null;
    }

    @Override
    public EdgeTraversal traversalEdge(Object... edgeIds) {
        return null;
    }

    @Override
    public Transaction transaction() {
        return null;
    }

    @Override
    public <T> Stream<T> gremlin(String gremlin) {
        return null;
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

}
