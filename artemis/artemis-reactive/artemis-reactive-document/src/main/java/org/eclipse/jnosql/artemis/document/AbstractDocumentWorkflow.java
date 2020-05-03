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
package org.eclipse.jnosql.artemis.document;

import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.document.DocumentWorkflow;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * The template method to {@link DocumentWorkflow}
 */
public abstract class AbstractDocumentWorkflow implements DocumentWorkflow {

    protected abstract DocumentEventPersistManager getDocumentEventPersistManager();


    protected abstract DocumentEntityConverter getConverter();


    @Override
    public <T> T flow(T entity, UnaryOperator<DocumentEntity> action) {

        Function<T, T> flow = getFlow(entity, action);

        return flow.apply(entity);

    }

    private <T> Function<T, T> getFlow(T entity, UnaryOperator<DocumentEntity> action) {
        UnaryOperator<T> validation = t -> Objects.requireNonNull(t, "entity is required");

        UnaryOperator<T> firePreEntity = t -> {
            getDocumentEventPersistManager().firePreEntity(t);
            return t;
        };

        UnaryOperator<T> firePreDocumentEntity = t -> {
            getDocumentEventPersistManager().firePreDocumentEntity(t);
            return t;
        };


        Function<T, DocumentEntity> converterDocument = t -> getConverter().toDocument(t);

        UnaryOperator<DocumentEntity> firePreDocument = t -> {
            getDocumentEventPersistManager().firePreDocument(t);
            return t;
        };

        UnaryOperator<DocumentEntity> firePostDocument = t -> {
            getDocumentEventPersistManager().firePostDocument(t);
            return t;
        };

        Function<DocumentEntity, T> converterEntity = t -> getConverter().toEntity(entity, t);

        UnaryOperator<T> firePostEntity = t -> {
            getDocumentEventPersistManager().firePostEntity(t);
            return t;
        };

        UnaryOperator<T> firePostDocumentEntity = t -> {
            getDocumentEventPersistManager().firePostDocumentEntity(t);
            return t;
        };


        return validation
                .andThen(firePreEntity)
                .andThen(firePreDocumentEntity)
                .andThen(converterDocument)
                .andThen(firePreDocument)
                .andThen(action)
                .andThen(firePostDocument)
                .andThen(converterEntity)
                .andThen(firePostEntity)
                .andThen(firePostDocumentEntity);
    }
}