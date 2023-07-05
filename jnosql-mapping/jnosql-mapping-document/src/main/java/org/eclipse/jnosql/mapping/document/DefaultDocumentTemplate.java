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
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

/**
 * The default implementation of DocumentTemplate
 */
@Default
@Database(DatabaseType.DOCUMENT)
@ApplicationScoped
class DefaultDocumentTemplate extends AbstractDocumentTemplate {

    private DocumentEntityConverter converter;

    private Instance<DocumentManager> manager;

    private DocumentWorkflow workflow;

    private DocumentEventPersistManager persistManager;

    private EntitiesMetadata entities;

    private Converters converters;

    @Inject
    DefaultDocumentTemplate(DocumentEntityConverter converter, Instance<DocumentManager> manager,
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
    protected DocumentManager getManager() {
        return manager.get();
    }

    @Override
    protected DocumentWorkflow getWorkflow() {
        return workflow;
    }

    @Override
    protected DocumentEventPersistManager getEventManager() {
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
