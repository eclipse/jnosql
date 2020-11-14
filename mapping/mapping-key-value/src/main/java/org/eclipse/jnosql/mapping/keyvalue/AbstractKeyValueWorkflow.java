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
package org.eclipse.jnosql.mapping.keyvalue;

import jakarta.nosql.keyvalue.KeyValueEntity;
import jakarta.nosql.mapping.keyvalue.KeyValueEntityConverter;
import jakarta.nosql.mapping.keyvalue.KeyValueEventPersistManager;
import jakarta.nosql.mapping.keyvalue.KeyValueWorkflow;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public abstract class AbstractKeyValueWorkflow implements KeyValueWorkflow {

    protected abstract KeyValueEventPersistManager getEventPersistManager();


    protected abstract KeyValueEntityConverter getConverter();


    @Override
    public <T> T flow(T entity, UnaryOperator<KeyValueEntity> action) {

        Function<T, T> flow = getFlow(entity, action);

        return flow.apply(entity);

    }

    private <T> Function<T, T> getFlow(T entity, UnaryOperator<KeyValueEntity> action) {
        UnaryOperator<T> validation = t -> Objects.requireNonNull(t, "entity is required");

        UnaryOperator<T> firePreEntity = t -> {
            getEventPersistManager().firePreEntity(t);
            return t;
        };

        UnaryOperator<T> firePreKeyValueEntity = t -> {
            getEventPersistManager().firePreKeyValueEntity(t);
            return t;
        };

        Function<T, KeyValueEntity> convertKeyValue = t -> getConverter().toKeyValue(t);

        UnaryOperator<KeyValueEntity> firePreDocument = t -> {
            getEventPersistManager().firePreKeyValue(t);
            return t;
        };

        UnaryOperator<KeyValueEntity> firePostDocument = t -> {
            getEventPersistManager().firePostKeyValue(t);
            return t;
        };

        Function<KeyValueEntity, T> converterEntity = t -> getConverter().toEntity((Class<T>) entity.getClass(), t);

        UnaryOperator<T> firePostEntity = t -> {
            getEventPersistManager().firePostEntity(t);
            return t;
        };

        UnaryOperator<T> firePostKeyValueEntity = t -> {
            getEventPersistManager().firePostKeyValueEntity(t);
            return t;
        };


        return validation
                .andThen(firePreEntity)
                .andThen(firePreKeyValueEntity)
                .andThen(convertKeyValue)
                .andThen(firePreDocument)
                .andThen(action)
                .andThen(firePostDocument)
                .andThen(converterEntity)
                .andThen(firePostEntity)
                .andThen(firePostKeyValueEntity);
    }
}