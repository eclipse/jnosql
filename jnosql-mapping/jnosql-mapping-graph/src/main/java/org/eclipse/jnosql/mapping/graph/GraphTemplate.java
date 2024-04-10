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

import org.eclipse.jnosql.mapping.PreparedStatement;
import org.apache.tinkerpop.gremlin.structure.Direction;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.eclipse.jnosql.mapping.semistructured.SemiStructuredTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;


/**
 * GraphTemplate is a helper class that increases productivity when performing common Graph operations.
 * Includes integrated object mapping between documents and POJOs {@link org.apache.tinkerpop.gremlin.structure.Vertex}
 * and {@link org.apache.tinkerpop.gremlin.structure.Edge}.
 * It represents the common operation between an entity and {@link org.apache.tinkerpop.gremlin.structure.Graph}
 *
 * @see org.apache.tinkerpop.gremlin.structure.Graph
 */
public interface GraphTemplate extends SemiStructuredTemplate {

    /**
     * Deletes a {@link org.apache.tinkerpop.gremlin.structure.Vertex}
     *
     * @param id  the id to be used in the query {@link org.apache.tinkerpop.gremlin.structure.T#id}
     * @param <T> the id type
     * @throws NullPointerException when id is null
     */
    <T> void delete(T id);

    /**
     * Deletes a {@link org.apache.tinkerpop.gremlin.structure.Edge}
     *
     * @param id  the id to be used in the query {@link org.apache.tinkerpop.gremlin.structure.T#id}
     * @param <T> the id type
     * @throws NullPointerException when either label and id are null
     */
    <T> void deleteEdge(T id);

    /**
     * Deletes {@link org.apache.tinkerpop.gremlin.structure.Edge} instances
     *
     * @param ids the ids to be used in the query {@link org.apache.tinkerpop.gremlin.structure.T#id}
     * @param <T> the id type
     * @throws NullPointerException when either label and id are null
     */
    <T> void deleteEdge(Iterable<T> ids);

    /**
     * Either find or create an Edge between this two entities.
     * {@link org.apache.tinkerpop.gremlin.structure.Edge}
     * <pre>entityOUT ---label---&#62; entityIN.</pre>
     *
     * @param incoming the incoming entity
     * @param label    the Edge label
     * @param outgoing the outgoing entity
     * @param <I>      the incoming type
     * @param <O>      the outgoing type
     * @return the {@link EdgeEntity} of these two entities
     * @throws NullPointerException                          Either when any elements are null or the entity is null
     */
    <O, I> EdgeEntity edge(O outgoing, String label, I incoming);

    /**
     * Either find or create an Edge between this two entities.
     * {@link org.apache.tinkerpop.gremlin.structure.Edge}
     * <pre>entityOUT ---label---&#62; entityIN.</pre>
     *
     * @param incoming the incoming entity
     * @param label    the Edge label
     * @param outgoing the outgoing entity
     * @param <I>      the incoming type
     * @param <O>      the outgoing type
     * @return the {@link EdgeEntity} of these two entities
     * @throws NullPointerException                          Either when any elements are null or the entity is null
     */
    default <O, I> EdgeEntity edge(O outgoing, Supplier<String> label, I incoming) {
        Objects.requireNonNull(label, "supplier is required");
        return edge(outgoing, label.get(), incoming);
    }

    /**
     * Find an entity given {@link org.apache.tinkerpop.gremlin.structure.T#label} and
     * {@link org.apache.tinkerpop.gremlin.structure.T#id}
     *
     * @param id  the id to be used in the query {@link org.apache.tinkerpop.gremlin.structure.T#id}
     * @param <T> the entity type
     * @param <K> the id type
     * @return the entity found otherwise {@link Optional#empty()}
     * @throws NullPointerException when id is null
     */
    <T, K> Optional<T> find(K id);

    /**
     * returns the edges of from a vertex id
     *
     * @param id        the id
     * @param direction the direction
     * @param labels    the edge labels
     * @param <K>       the K type
     * @return the Edges
     * @throws NullPointerException where there is any parameter null
     */
    <K> Collection<EdgeEntity> edgesById(K id, Direction direction, String... labels);

    /**
     * returns the edges of from a vertex id
     *
     * @param id        the id
     * @param direction the direction
     * @param labels    the edge labels
     * @param <K>       the K type
     * @return the Edges
     * @throws NullPointerException where there is any parameter null
     */
    <K> Collection<EdgeEntity> edgesById(K id, Direction direction, Supplier<String>... labels);

    /**
     * returns the edges of from a vertex id
     *
     * @param id        the id
     * @param direction the direction
     * @param <K>       the K type
     * @return the Edges
     * @throws NullPointerException where there is any parameter null
     */
    <K> Collection<EdgeEntity> edgesById(K id, Direction direction);

    /**
     * returns the edges of from an entity
     *
     * @param entity    the entity
     * @param direction the direction
     * @param labels    the edge labels
     * @param <T>       the entity type
     * @return the Edges
     * @throws NullPointerException where there is any parameter null
     */
    <T> Collection<EdgeEntity> edges(T entity, Direction direction, String... labels);

    /**
     * returns the edges of from an entity
     *
     * @param entity    the entity
     * @param direction the direction
     * @param labels    the edge labels
     * @param <T>       the entity type
     * @return the Edges
     * @throws NullPointerException where there is any parameter null
     */
    <T> Collection<EdgeEntity> edges(T entity, Direction direction, Supplier<String>... labels);

    /**
     * returns the edges of from an entity
     *
     * @param entity    the entity
     * @param direction the direction
     * @param <T>       the entity type
     * @return the Edges
     * @throws NullPointerException where there is any parameter null
     */
    <T> Collection<EdgeEntity> edges(T entity, Direction direction);

    /**
     * Finds an {@link EdgeEntity} from the Edge Id
     *
     * @param edgeId the edge id
     * @param <E>    the edge id type
     * @return the {@link EdgeEntity} otherwise {@link Optional#empty()}
     * @throws IllegalStateException when edgeId is null
     */
    <E> Optional<EdgeEntity> edge(E edgeId);


    /**
     * Gets a {@link VertexTraversal} to run a query in the graph
     *
     * @param vertexIds get ids
     * @return a {@link VertexTraversal} instance
     * @throws IllegalStateException if any id element is null
     */
    VertexTraversal traversalVertex(Object... vertexIds);


    /**
     * Gets a {@link EdgeTraversal} to run a query in the graph
     *
     * @param edgeIds get ids
     * @return a {@link VertexTraversal} instance
     * @throws IllegalStateException if any id element is null
     */
    EdgeTraversal traversalEdge(Object... edgeIds);

    /**
     * Gets the current transaction
     *
     * @return the current {@link Transaction}
     */
    Transaction transaction();

    /**
     * Executes a Gremlin then bring the result as a {@link Stream}
     *
     * @param gremlin the query gremlin
     * @param <T>     the entity type
     * @return the result as {@link Stream}
     * @throws NullPointerException when the gremlin is null
     */
    <T> Stream<T> gremlin(String gremlin);

    /**
     * Executes a Gremlin query then bring the result as a unique result
     *
     * @param gremlin the gremlin query
     * @param <T>     the entity type
     * @return the result as {@link List}
     * @throws NullPointerException     when the query is null
     */
    <T> Optional<T> gremlinSingleResult(String gremlin);

    /**
     * Creates a {@link PreparedStatement} from the query
     *
     * @param gremlin the gremlin query
     * @return a {@link PreparedStatement} instance
     * @throws NullPointerException when the query is null
     */
    PreparedStatement gremlinPrepare(String gremlin);

}
