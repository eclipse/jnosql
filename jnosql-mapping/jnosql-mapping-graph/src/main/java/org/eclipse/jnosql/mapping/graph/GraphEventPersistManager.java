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


import org.apache.tinkerpop.gremlin.structure.Vertex;

/**
 * This interface represent the manager of events. When an entity be either saved or updated an event will be fired. This order gonna be:
 * 1) firePreEntity
 * 2) firePreGraphEntity
 * 3) firePreGraph
 * 4) firePostGraph
 * 5) firePostEntity
 * 6) firePostGraphEntity
 *
 * @see GraphWorkflow
 */
public interface GraphEventPersistManager {


    /**
     * Fire an event once the method is called
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreEntity(T entity);

    /**
     * Fire an event after convert the {@link Vertex},
     * from database response, to Entity.
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostEntity(T entity);


    /**
     * Fire an event once the method is called after firePreEntity
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreGraphEntity(T entity);

    /**
     * Fire an event after firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostGraphEntity(T entity);
}
