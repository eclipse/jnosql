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

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.eclipse.jnosql.communication.Value;
import org.eclipse.jnosql.communication.graph.CommunicationEntityConverter;
import org.eclipse.jnosql.communication.semistructured.Element;
import org.eclipse.jnosql.mapping.semistructured.EntityConverter;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * It is a wrapper of {@link org.apache.tinkerpop.gremlin.structure.Edge} that links two Entity.
 * Along with its Property objects, an Edge has both a Direction and a label.
 * Any Change at the Edge is automatically continued in the database. However, any, change in the Entity will be ignored.
 * {@link GraphTemplate#update(Object)}
 *
 * <pre>outVertex ---label---&#62; inVertex.</pre>
 */
public interface EdgeEntity {
    /**
     * Returns the identifier of this edge.
     *
     * @return the identifier of this edge
     */
    Object id();

    /**
     * Returns the identifier of this edge converted to the specified type.
     *
     * @param type the class type to convert the identifier to
     * @param <T>  the type to convert the identifier to
     * @return the identifier of this edge converted to the specified type
     */
    <T> T id(Class<T> type);

    /**
     * Returns the label of this edge.
     *
     * @return the label of this edge
     */
    String label();

    /**
     * Retrieves the incoming entity connected to this edge.
     *
     * @param <T> the type of the incoming entity
     * @return the incoming entity connected to this edge
     */
    <T> T incoming();

    /**
     * Retrieves the outgoing entity connected to this edge.
     *
     * @param <T> the type of the outgoing entity
     * @return the outgoing entity connected to this edge
     */
    <T> T outgoing();

    /**
     * Returns the properties of this edge.
     *
     * @return the properties of this edge
     */
    List<Element> properties();

    /**
     * Adds a new property to this edge with the specified key and value.
     *
     * @param key   the key of the property
     * @param value the value of the property
     * @throws NullPointerException if either key or value is null
     */
    void add(String key, Object value);

    /**
     * Adds a new property to this edge with the specified key and value.
     *
     * @param key   the key of the property
     * @param value the value of the property
     * @throws NullPointerException if either key or value is null
     */
    void add(String key, Value value);

    /**
     * Removes the property with the specified key from this edge.
     *
     * @param key the key of the property to be removed
     * @throws NullPointerException if the key is null
     */
    void remove(String key);

    /**
     * Returns the property value associated with the specified key, if present.
     *
     * @param key the key of the property to retrieve
     * @return the value associated with the specified key, or {@link Optional#empty()} if the key is not present
     * @throws NullPointerException if the key is null
     */
    Optional<Value> get(String key);

    /**
     * Returns true if this edge contains no properties.
     *
     * @return true if this edge contains no properties
     */
    boolean isEmpty();

    /**
     * Returns the number of properties in this edge.
     *
     * @return the number of properties in this edge
     */
    int size();

    /**
     * Deletes this edge from the database. After this operation, any modification attempts such as adding or removing properties will result in an illegal state.
     */
    void delete();

    /**
     * Creates an {@link EdgeEntity} instance from the provided {@link EntityConverter} and {@link Edge}.
     *
     * @param converter the entity converter to use
     * @param edge      the edge to create the entity from
     * @return the created {@link EdgeEntity} instance
     * @throws NullPointerException if either converter or edge is null
     */
    static EdgeEntity of(EntityConverter converter, Edge edge) {
        Objects.requireNonNull(converter, "converter is required");
        Objects.requireNonNull(edge, "edge is required");
        var entityConverter = CommunicationEntityConverter.INSTANCE;
        return new DefaultEdgeEntity<>(edge, converter.toEntity(entityConverter.apply(edge.inVertex())),
                converter.toEntity(entityConverter.apply(edge.outVertex())));
    }

}
