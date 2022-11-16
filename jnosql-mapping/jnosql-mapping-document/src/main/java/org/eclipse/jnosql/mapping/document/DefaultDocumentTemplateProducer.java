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


import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateProducer;
import jakarta.nosql.mapping.document.DocumentWorkflow;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import java.util.Objects;

/**
 * The default implementation of {@link DocumentTemplateProducer}
 */
@ApplicationScoped
class DefaultDocumentTemplateProducer implements DocumentTemplateProducer {


    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private DocumentWorkflow workflow;

    @Inject
    private DocumentEventPersistManager persistManager;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;


    @Override
    public DocumentTemplate get(DocumentManager collectionManager) {
        Objects.requireNonNull(collectionManager, "collectionManager is required");
        return new ProducerDocumentTemplate(converter, collectionManager, workflow,
                persistManager, entities, converters);
    }

    @Vetoed
    static class ProducerDocumentTemplate extends AbstractDocumentTemplate {

        private DocumentEntityConverter converter;

        private DocumentManager manager;

        private DocumentWorkflow workflow;

        private DocumentEventPersistManager persistManager;

        private Converters converters;

        private EntitiesMetadata entities;
        ProducerDocumentTemplate(DocumentEntityConverter converter, DocumentManager manager,
                                 DocumentWorkflow workflow,
                                 DocumentEventPersistManager persistManager,
                                 EntitiesMetadata entities, Converters converters) {
            this.converter = converter;
            this.manager = manager;
            this.workflow = workflow;
            this.persistManager = persistManager;
            this.entities = entities;
            this.converters = converters;
        }

        ProducerDocumentTemplate() {
        }

        @Override
        protected DocumentEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected DocumentManager getManager() {
            return manager;
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
}
