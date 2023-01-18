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


import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.document.DocumentQuery;
import org.eclipse.jnosql.mapping.EntityPostPersist;
import org.eclipse.jnosql.mapping.EntityPrePersist;

/**
 * The default implementation of {@link DocumentEventPersistManager}
 */
@ApplicationScoped
public class DocumentEventPersistManager {

    @Inject
    private Event<DocumentEntityPrePersist> documentEntityPrePersistEvent;

    @Inject
    private Event<DocumentEntityPostPersist> documentEntityPostPersistEvent;

    @Inject
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Inject
    private Event<EntityPostPersist> entityPostPersistEvent;

    @Inject
    private Event<EntityDocumentPrePersist> entityDocumentPrePersist;

    @Inject
    private Event<EntityDocumentPostPersist> entityDocumentPostPersist;

    @Inject
    private Event<DocumentQueryExecute> documentQueryExecute;

    @Inject
    private Event<DocumentDeleteQueryExecute> documentDeleteQueryExecute;

    /**
     * Fire an event after the conversion of the entity to communication API model.
     *
     * @param entity the entity
     */
    public void firePreDocument(DocumentEntity entity) {
        documentEntityPrePersistEvent.fire(new DocumentEntityPrePersist(entity));
    }

    /**
     * Fire an event after the response from communication layer
     *
     * @param entity the entity
     */
    public void firePostDocument(DocumentEntity entity) {
        documentEntityPostPersistEvent.fire(new DocumentEntityPostPersist(entity));
    }

    /**
     * Fire an event after convert the {@link DocumentEntity},
     * from database response, to Entity.
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(EntityPrePersist.of(entity));
    }

    /**
     * Fire an event after convert the {@link DocumentEntity},
     * from database response, to Entity.
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    public <T> void firePostEntity(T entity) {
        entityPostPersistEvent.fire(EntityPostPersist.of(entity));
    }

    /**
     * Fire an event after firePreEntity
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    public <T> void firePreDocumentEntity(T entity) {
        entityDocumentPrePersist.fire(new EntityDocumentPrePersist(entity));
    }

    /**
     * Fire an event after firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    public <T> void firePostDocumentEntity(T entity) {
        entityDocumentPostPersist.fire(new EntityDocumentPostPersist(entity));
    }

    /**
     * Fire an event before the query is executed
     *
     * @param query the query
     */
    public void firePreQuery(DocumentQuery query) {
        documentQueryExecute.fire(new DocumentQueryExecute(query));
    }

    /**
     * Fire an event before the delete query is executed
     *
     * @param query the query
     */
    public void firePreDeleteQuery(DocumentDeleteQuery query) {
        documentDeleteQueryExecute.fire(new DocumentDeleteQueryExecute(query));
    }
}
