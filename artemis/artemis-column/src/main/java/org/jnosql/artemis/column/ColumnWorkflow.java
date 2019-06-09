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


import org.jnosql.diana.column.ColumnEntity;

import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on {@link ColumnTemplate}.
 * The default implementation follows:
 *  <p>{@link ColumnEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link ColumnEventPersistManager#firePreColumnEntity(Object)}</p>
 *  <p>{@link ColumnEntityConverter#toColumn(Object)}</p>
 *  <p>{@link ColumnEventPersistManager#firePreColumn(ColumnEntity)}</p>
 *  <p>Database alteration</p>
 *  <p>{@link ColumnEventPersistManager#firePostColumn(ColumnEntity)}</p>
 *  <p>{@link ColumnEventPersistManager#firePostEntity(Object)}</p>
 *  <p>{@link ColumnEventPersistManager#firePostColumnEntity(Object)}</p>
 */
public interface ColumnWorkflow {

    /**
     * Executes the workflow to do an interaction on a database column family.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the the entity response
     * @see ColumnTemplate#insert(Object, java.time.Duration) ColumnTemplate#insert(Object)
     * ColumnTemplate#update(Object)
     */
    <T> T flow(T entity, UnaryOperator<ColumnEntity> action);

}
