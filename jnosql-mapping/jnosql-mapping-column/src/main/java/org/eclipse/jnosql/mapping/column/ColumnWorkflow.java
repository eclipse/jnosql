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
package org.eclipse.jnosql.mapping.column;

import org.eclipse.jnosql.communication.column.ColumnEntity;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on {@link jakarta.nosql.column.ColumnTemplate}.
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
public abstract class ColumnWorkflow {

    protected abstract ColumnEventPersistManager getEventManager();


    protected abstract ColumnEntityConverter getConverter();

    /**
     * Executes the workflow to do an interaction on a database column family.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the entity response
     * @see jakarta.nosql.column.ColumnTemplate#insert(Object, java.time.Duration) ColumnTemplate#insert(Object)
     * ColumnTemplate#update(Object)
     */
    public <T> T flow(T entity, UnaryOperator<ColumnEntity> action) {

        Function<T, T> flow = getFlow(entity, action);

        return flow.apply(entity);

    }

    private <T> Function<T, T> getFlow(T entity, UnaryOperator<ColumnEntity> action) {
        UnaryOperator<T> validation = t -> Objects.requireNonNull(t, "entity is required");

        UnaryOperator<T> firePreEntity = t -> {
            getEventManager().firePreEntity(t);
            return t;
        };

        UnaryOperator<T> firePreColumnEntity = t -> {
            getEventManager().firePreColumnEntity(t);
            return t;
        };

        Function<T, ColumnEntity> converterColumn = t -> getConverter().toColumn(t);

        UnaryOperator<ColumnEntity> firePreColumn = t -> {
            getEventManager().firePreColumn(t);
            return t;
        };

        UnaryOperator<ColumnEntity> firePostColumn = t -> {
            getEventManager().firePostColumn(t);
            return t;
        };

        Function<ColumnEntity, T> converterEntity = t -> getConverter().toEntity(entity, t);

        UnaryOperator<T> firePostEntity = t -> {
            getEventManager().firePostEntity(t);
            return t;
        };

        UnaryOperator<T> firePostColumnEntity = t -> {
            getEventManager().firePostColumnEntity(t);
            return t;
        };

        return validation
                .andThen(firePreEntity)
                .andThen(firePreColumnEntity)
                .andThen(converterColumn)
                .andThen(firePreColumn)
                .andThen(action)
                .andThen(firePostColumn)
                .andThen(converterEntity)
                .andThen(firePostEntity)
                .andThen(firePostColumnEntity);
    }
}
