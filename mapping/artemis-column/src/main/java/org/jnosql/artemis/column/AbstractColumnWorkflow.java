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

import org.jnosql.diana.api.column.ColumnEntity;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * The {@link ColumnWorkflow} template method
 */
public abstract class AbstractColumnWorkflow implements ColumnWorkflow {

    protected abstract ColumnEventPersistManager getColumnEventPersistManager();


    protected abstract ColumnEntityConverter getConverter();

    public <T> T flow(T entity, UnaryOperator<ColumnEntity> action) {

        Function<T, T> flow = getFlow(entity, action);

        return flow.apply(entity);

    }

    private <T> Function<T, T> getFlow(T entity, UnaryOperator<ColumnEntity> action) {
        UnaryOperator<T> validation = t -> Objects.requireNonNull(t, "entity is required");

        UnaryOperator<T> firePreEntity = t -> {
            getColumnEventPersistManager().firePreEntity(t);
            return t;
        };

        UnaryOperator<T> firePreColumnEntity = t -> {
            getColumnEventPersistManager().firePreColumnEntity(t);
            return t;
        };

        Function<T, ColumnEntity> converterColumn = t -> getConverter().toColumn(t);

        UnaryOperator<ColumnEntity> firePreColumn = t -> {
            getColumnEventPersistManager().firePreColumn(t);
            return t;
        };

        UnaryOperator<ColumnEntity> firePostColumn = t -> {
            getColumnEventPersistManager().firePostColumn(t);
            return t;
        };

        Function<ColumnEntity, T> converterEntity = t -> getConverter().toEntity(entity, t);

        UnaryOperator<T> firePostEntity = t -> {
            getColumnEventPersistManager().firePostEntity(t);
            return t;
        };

        UnaryOperator<T> firePostColumnEntity = t -> {
            getColumnEventPersistManager().firePostColumnEntity(t);
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
