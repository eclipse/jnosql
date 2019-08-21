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


import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateProducer;
import jakarta.nosql.mapping.document.DocumentWorkflow;
import jakarta.nosql.mapping.reflection.ClassMappings;

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
    private ClassMappings classMappings;

    @Inject
    private Converters converters;


    @Override
    public DocumentTemplate get(DocumentCollectionManager collectionManager) {
        Objects.requireNonNull(collectionManager, "collectionManager is required");
        return new ProducerDocumentTemplate(converter, collectionManager, workflow,
                persistManager, classMappings, converters);
    }

    @Vetoed
    static class ProducerDocumentTemplate extends AbstractDocumentTemplate {

        private DocumentEntityConverter converter;

        private DocumentCollectionManager manager;

        private DocumentWorkflow workflow;

        private DocumentEventPersistManager persistManager;

        private Converters converters;

        private ClassMappings classMappings;
        ProducerDocumentTemplate(DocumentEntityConverter converter, DocumentCollectionManager manager,
                                 DocumentWorkflow workflow,
                                 DocumentEventPersistManager persistManager,
                                 ClassMappings classMappings, Converters converters) {
            this.converter = converter;
            this.manager = manager;
            this.workflow = workflow;
            this.persistManager = persistManager;
            this.classMappings = classMappings;
            this.converters = converters;
        }

        ProducerDocumentTemplate() {
        }

        @Override
        protected DocumentEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected DocumentCollectionManager getManager() {
            return manager;
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
        protected ClassMappings getClassMappings() {
            return classMappings;
        }

        @Override
        protected Converters getConverters() {
            return converters;
        }
    }
}
