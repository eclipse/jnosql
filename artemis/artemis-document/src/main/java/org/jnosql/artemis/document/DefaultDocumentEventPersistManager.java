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
package org.jnosql.artemis.document;


import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.EntityPostPersit;
import jakarta.nosql.mapping.EntityPrePersist;
import jakarta.nosql.mapping.document.DocumentDeleteQueryExecute;
import jakarta.nosql.mapping.document.DocumentEntityPostPersist;
import jakarta.nosql.mapping.document.DocumentEntityPrePersist;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.document.DocumentQueryExecute;
import jakarta.nosql.mapping.document.EntityDocumentPostPersist;
import jakarta.nosql.mapping.document.EntityDocumentPrePersist;
import org.jnosql.artemis.DefaultEntityPostPersist;
import org.jnosql.artemis.DefaultEntityPrePersist;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * The default implementation of {@link DocumentEventPersistManager}
 */
@ApplicationScoped
class DefaultDocumentEventPersistManager implements DocumentEventPersistManager {

    @Inject
    private Event<DocumentEntityPrePersist> documentEntityPrePersistEvent;

    @Inject
    private Event<DocumentEntityPostPersist> documentEntityPostPersistEvent;

    @Inject
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Inject
    private Event<EntityPostPersit> entityPostPersitEvent;

    @Inject
    private Event<EntityDocumentPrePersist> entityDocumentPrePersist;

    @Inject
    private Event<EntityDocumentPostPersist> entityDocumentPostPersist;

    @Inject
    private Event<DocumentQueryExecute> documentQueryExecute;

    @Inject
    private Event<DocumentDeleteQueryExecute> documentDeleteQueryExecute;

    @Override
    public void firePreDocument(DocumentEntity entity) {
        documentEntityPrePersistEvent.fire(new DefaultDocumentEntityPrePersist(entity));
    }

    @Override
    public void firePostDocument(DocumentEntity entity) {
        documentEntityPostPersistEvent.fire(new DefaultDocumentEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(new DefaultEntityPrePersist(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersitEvent.fire(new DefaultEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreDocumentEntity(T entity) {
        entityDocumentPrePersist.fire(new DefaultEntityDocumentPrePersist(entity));
    }

    @Override
    public <T> void firePostDocumentEntity(T entity) {
        entityDocumentPostPersist.fire(new DefaultEntityDocumentPostPersist(entity));
    }

    @Override
    public void firePreQuery(DocumentQuery query) {
        documentQueryExecute.fire(new DefaultDocumentQueryExecute(query));
    }

    @Override
    public void firePreDeleteQuery(DocumentDeleteQuery query) {
        documentDeleteQueryExecute.fire(new DefaultDocumentDeleteQueryExecute(query));
    }
}
