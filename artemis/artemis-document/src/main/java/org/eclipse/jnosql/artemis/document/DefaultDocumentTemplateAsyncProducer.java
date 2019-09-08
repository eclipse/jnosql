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


import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentTemplateAsync;
import jakarta.nosql.mapping.document.DocumentTemplateAsyncProducer;
import jakarta.nosql.mapping.reflection.ClassMappings;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
import java.util.Objects;

/**
 * The default implementation of {@link DocumentTemplateAsyncProducer}
 */
@ApplicationScoped
class DefaultDocumentTemplateAsyncProducer implements DocumentTemplateAsyncProducer {


    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Override
    public DocumentTemplateAsync get(DocumentCollectionManagerAsync collectionManager) {
        Objects.requireNonNull(collectionManager, "collectionManager is required");
        return new ProducerAbstractDocumentTemplateAsync(converter, collectionManager, classMappings, converters);
    }

    @Vetoed
    static class ProducerAbstractDocumentTemplateAsync extends AbstractDocumentTemplateAsync {

        private DocumentEntityConverter converter;

        private DocumentCollectionManagerAsync manager;

        private ClassMappings classMappings;

        private Converters converters;

        ProducerAbstractDocumentTemplateAsync(DocumentEntityConverter converter,
                                              DocumentCollectionManagerAsync manager,
                                              ClassMappings classMappings,
                                              Converters converters) {
            this.converter = converter;
            this.manager = manager;
            this.classMappings = classMappings;
            this.converters = converters;
        }

        ProducerAbstractDocumentTemplateAsync() {
        }

        @Override
        protected DocumentEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected DocumentCollectionManagerAsync getManager() {
            return manager;
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
