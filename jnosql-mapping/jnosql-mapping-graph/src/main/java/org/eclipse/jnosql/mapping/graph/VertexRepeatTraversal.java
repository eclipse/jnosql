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

import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.structure.T;

import java.util.function.Supplier;

import static java.util.Objects.requireNonNull;

/**
 * The wrapper step to
 * {@link org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal#repeat(org.apache.tinkerpop.gremlin.process.traversal.Traversal)}
 * in the Vertex type.
 */
public interface VertexRepeatTraversal {

    /**
     * Adds a equals condition to a query
     *
     * @param propertyKey the key
     * @param value       the value to the condition
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when either key or value are null
     */
    VertexRepeatStepTraversal has(String propertyKey, Object value);

    /**
     * Adds a equals condition to a query
     *
     * @param propertyKey the key
     * @param predicate   the predicate condition
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when either key or predicate condition are null
     */
    VertexRepeatStepTraversal has(String propertyKey, P<?> predicate);

    /**
     * Adds a equals condition to a query
     *
     * @param propertyKey the key
     * @param value       the value to the condition
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when either key or value are null
     */
    default VertexRepeatStepTraversal has(Supplier<String> propertyKey, Object value){
        requireNonNull(propertyKey, "the supplier is required");
        return has(propertyKey.get(), value);
    }

    /**
     * Adds a equals condition to a query
     *
     * @param propertyKey the key
     * @param predicate   the predicate condition
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when either key or predicate condition are null
     */
    default VertexRepeatStepTraversal has(Supplier<String> propertyKey, P<?> predicate){
        requireNonNull(propertyKey, "the supplier is required");
        return has(propertyKey.get(), predicate);
    }

    /**
     * Adds a equals condition to a query
     *
     * @param accessor the key
     * @param value    the value to the condition
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when either key or value are null
     */
    VertexRepeatStepTraversal has(T accessor, Object value);

    /**
     * Adds a equals condition to a query
     *
     * @param accessor  the key
     * @param predicate the predicate condition
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when either key or value are null
     */
    VertexRepeatStepTraversal has(T accessor, P<?> predicate);


    /**
     * Defines Vertex has not a property
     *
     * @param propertyKey the property key
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when propertyKey is null
     */
    VertexRepeatStepTraversal hasNot(String propertyKey);

    /**
     * Defines Vertex has not a property
     *
     * @param propertyKey the property key
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when propertyKey is null
     */
    default VertexRepeatStepTraversal hasNot(Supplier<String> propertyKey){
        requireNonNull(propertyKey, "the supplier is required");
        return hasNot(propertyKey.get());
    }

    /**
     * Map the {@link VertexTraversal} to its outgoing adjacent vertices given the edge labels.
     *
     * @param labels the edge labels to traverse
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when has any null element
     */
    VertexRepeatStepTraversal out(String... labels);


    /**
     * Map the {@link VertexTraversal} to its adjacent vertices given the edge labels.
     *
     * @param labels the edge labels to traverse
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when has any null element
     */
    VertexRepeatStepTraversal in(String... labels);


    /**
     * Map the {@link VertexTraversal} to its incoming adjacent vertices given the edge labels.
     *
     * @param labels the edge labels to traverse
     * @return a {@link VertexRepeatStepTraversal} with the new condition
     * @throws NullPointerException when has any null element
     */
    VertexRepeatStepTraversal both(String... labels);

}
