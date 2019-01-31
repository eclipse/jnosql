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


import org.jnosql.artemis.EntityPostPersit;
import org.jnosql.artemis.EntityPrePersist;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;

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
        documentEntityPrePersistEvent.fire(DocumentEntityPrePersist.of(entity));
    }

    @Override
    public void firePostDocument(DocumentEntity entity) {
        documentEntityPostPersistEvent.fire(DocumentEntityPostPersist.of(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(EntityPrePersist.of(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersitEvent.fire(EntityPostPersit.of(entity));
    }

    @Override
    public <T> void firePreDocumentEntity(T entity) {
        entityDocumentPrePersist.fire(EntityDocumentPrePersist.of(entity));
    }

    @Override
    public <T> void firePostDocumentEntity(T entity) {
        entityDocumentPostPersist.fire(EntityDocumentPostPersist.of(entity));
    }

    @Override
    public void firePreQuery(DocumentQuery query) {
        documentQueryExecute.fire(DocumentQueryExecute.of(query));
    }

    @Override
    public void firePreDeleteQuery(DocumentDeleteQuery query) {
        documentDeleteQueryExecute.fire(DocumentDeleteQueryExecute.of(query));
    }
}
