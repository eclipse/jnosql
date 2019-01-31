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
package org.jnosql.artemis.column;


import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnQuery;

/**
 * This interface represent the manager of events. When an entity be either saved or updated an event will be fired. This order gonna be:
 * 1) firePreEntity
 * 2) firePreColumnEntity
 * 3) firePreColumn
 * 4) firePostColumn
 * 5) firePostEntity
 * 6) firePostColumnEntity
 *
 * @see ColumnWorkflow
 */
public interface ColumnEventPersistManager {

    /**
     * Fire an event after the conversion of the entity to communication API model.
     *
     * @param entity the entity
     */
    void firePreColumn(ColumnEntity entity);

    /**
     * Fire an event after the response from communication layer
     *
     * @param entity the entity
     */
    void firePostColumn(ColumnEntity entity);

    /**
     * Fire an event once the method is called
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    <T> void firePreEntity(T entity);

    /**
     * Fire an event after convert the {@link org.jnosql.diana.api.column.ColumnEntity},
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
    <T> void firePreColumnEntity(T entity);

    /**
     * Fire an event after firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    <T> void firePostColumnEntity(T entity);

    /**
     * Fire an event before the query is executed
     *
     * @param query the query
     */
    void firePreQuery(ColumnQuery query);

    /**
     * Fire an event before the delete query is executed
     *
     * @param query the query
     */
    void firePreDeleteQuery(ColumnDeleteQuery query);
}
