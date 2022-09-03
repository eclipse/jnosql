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
package org.eclipse.jnosql.mapping.document;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.document.DocumentWorkflow;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * The default implementation of {@link jakarta.nosql.mapping.document.DocumentTemplate}
 */
@SuppressWarnings("unchecked")
@ApplicationScoped
class DefaultDocumentTemplate extends AbstractDocumentTemplate {

    private DocumentEntityConverter converter;

    private Instance<DocumentCollectionManager> manager;

    private DocumentWorkflow workflow;

    private DocumentEventPersistManager persistManager;

    private EntitiesMetadata entities;

    private Converters converters;

    @Inject
    DefaultDocumentTemplate(DocumentEntityConverter converter, Instance<DocumentCollectionManager> manager,
                            DocumentWorkflow workflow, DocumentEventPersistManager persistManager,
                            EntitiesMetadata entities, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.workflow = workflow;
        this.persistManager = persistManager;
        this.entities = entities;
        this.converters = converters;
    }

    DefaultDocumentTemplate() {
    }

    @Override
    protected DocumentEntityConverter getConverter() {
        return converter;
    }

    @Override
    protected DocumentCollectionManager getManager() {
        return manager.get();
    }

    @Override
    protected DocumentWorkflow getWorkflow() {
        return workflow;
    }

    @Override
    protected DocumentEventPersistManager getPersistManager() {
        return persistManager;
    }

    @Override
    protected EntitiesMetadata getEntities() {
        return entities;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

}
