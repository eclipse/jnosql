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



import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * This implementation defines the workflow to insert an Entity on {@link jakarta.nosql.document.DocumentTemplate}.
 * The default implementation follows:
 *  <p>{@link DocumentEventPersistManager#firePreEntity(Object)}</p>
 *  <p>{@link DocumentEntityConverter#toDocument(Object)}</p>
 *  <p>Database alteration</p>
 *  <p>{@link DocumentEventPersistManager#firePostEntity(Object)}</p>
 */
@ApplicationScoped
class DefaultDocumentWorkflow extends DocumentWorkflow {

    private DocumentEventPersistManager documentEventPersistManager;


    private DocumentEntityConverter converter;

    DefaultDocumentWorkflow() {
    }

    @Inject
    DefaultDocumentWorkflow(DocumentEventPersistManager documentEventPersistManager, DocumentEntityConverter converter) {
        this.documentEventPersistManager = documentEventPersistManager;
        this.converter = converter;
    }


    @Override
    protected DocumentEventPersistManager getEventManager() {
        return documentEventPersistManager;
    }

    @Override
    protected DocumentEntityConverter getConverter() {
        return converter;
    }
}
