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
package org.eclipse.jnosql.mapping.graph;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.List;

public interface GraphConverter {

    /**
     * Converts entity object to  TinkerPop Vertex
     *
     * @param entity the entity
     * @param <T>    the entity type
     * @return the ThinkerPop Vertex with the entity values
     * @throws NullPointerException when entity is null
     */
    <T> Vertex toVertex(T entity);

    /**
     * Converts vertex to an entity
     *
     * @param vertex the vertex
     * @param <T>    the entity type
     * @return a entity instance
     * @throws NullPointerException when vertex is null
     */
    <T> T toEntity(Vertex vertex);

    /**
     * Converts vertex to an entity
     *
     * @param type the entity class
     * @param vertex      the vertex
     * @param <T>         the entity type
     * @return a entity instance
     * @throws NullPointerException when vertex or type is null
     */
    <T> T toEntity(Class<T> type, Vertex vertex);

    /**
     *List the fields in the entity as property exclude fields annotated with {@link jakarta.nosql.mapping.Id}
     * @param entity the entity
     * @param <T> the entity type
     * @throws NullPointerException when entity is null
     * @return the properties of an entity
     */
    <T> List<Property<?>> getProperties(T entity);

    /**
     * Converts vertex to an entity
     * Instead of creating a new object is uses the instance used in this parameters
     *
     * @param type the entity class
     * @param vertex         the vertex
     * @param <T>            the entity type
     * @return a entity instance
     * @throws NullPointerException when vertex or type is null
     */
    <T> T toEntity(T type, Vertex vertex);

    /**
     * Converts {@link EdgeEntity} from {@link Edge} Thinkerpop
     *
     * @param edge the ThinkerPop edge
     * @return an EdgeEntity instance
     * @throws NullPointerException when Edge is null
     */
    EdgeEntity toEdgeEntity(Edge edge);

    /**
     * Converts {@link Edge} from {@link EdgeEntity}
     *
     * @param edge the EdgeEntity instance
     * @return a Edge instance
     * @throws NullPointerException when edge entity is null
     */
    Edge toEdge(EdgeEntity edge);


}
