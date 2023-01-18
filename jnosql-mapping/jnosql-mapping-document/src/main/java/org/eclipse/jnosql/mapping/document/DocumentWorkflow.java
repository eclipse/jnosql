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
package org.eclipse.jnosql.mapping.document;

import org.eclipse.jnosql.communication.document.DocumentEntity;


import java.util.Objects;
import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * This implementation defines the workflow to insert an Entity on DocumentTemplate.
 * The default implementation follows:
 *  <p>{@link DocumentEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link DocumentEventPersistManager#firePreDocumentEntity(Object)}</p>
 *  <p>{@link DocumentEntityConverter#toDocument(Object)}</p>
 *  <p>{@link DocumentEventPersistManager#firePreDocument(DocumentEntity)}}</p>
 *  <p>Database alteration</p>
 *  <p>{@link DocumentEventPersistManager#firePostDocument(DocumentEntity)}</p>
 *  <p>{@link DocumentEventPersistManager#firePostEntity(Object)}</p>
 *  <p>{@link DocumentEventPersistManager#firePostDocumentEntity(Object)}</p>
 */
public abstract class DocumentWorkflow {

    protected abstract DocumentEventPersistManager getEventManager();


    protected abstract DocumentEntityConverter getConverter();

    /**
     * Executes the workflow to do an interaction on a database document collection.
     *
     * @param entity the entity to be saved
     * @param action the alteration to be executed on database
     * @param <T>    the entity type
     * @return after the workflow the entity response
     */

    public <T> T flow(T entity, UnaryOperator<DocumentEntity> action) {

        Function<T, T> flow = getFlow(entity, action);

        return flow.apply(entity);

    }

    private <T> Function<T, T> getFlow(T entity, UnaryOperator<DocumentEntity> action) {
        UnaryOperator<T> validation = t -> Objects.requireNonNull(t, "entity is required");

        UnaryOperator<T> firePreEntity = t -> {
            getEventManager().firePreEntity(t);
            return t;
        };

        UnaryOperator<T> firePreDocumentEntity = t -> {
            getEventManager().firePreDocumentEntity(t);
            return t;
        };


        Function<T, DocumentEntity> converterDocument = t -> getConverter().toDocument(t);

        UnaryOperator<DocumentEntity> firePreDocument = t -> {
            getEventManager().firePreDocument(t);
            return t;
        };

        UnaryOperator<DocumentEntity> firePostDocument = t -> {
            getEventManager().firePostDocument(t);
            return t;
        };

        Function<DocumentEntity, T> converterEntity = t -> getConverter().toEntity(entity, t);

        UnaryOperator<T> firePostEntity = t -> {
            getEventManager().firePostEntity(t);
            return t;
        };

        UnaryOperator<T> firePostDocumentEntity = t -> {
            getEventManager().firePostDocumentEntity(t);
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