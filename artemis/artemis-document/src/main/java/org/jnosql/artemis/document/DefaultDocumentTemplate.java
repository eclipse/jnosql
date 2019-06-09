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

import org.jnosql.artemis.Converters;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.document.DocumentCollectionManager;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * The default implementation of {@link DocumentTemplate}
 */
@SuppressWarnings("unchecked")
class DefaultDocumentTemplate extends AbstractDocumentTemplate {


    private DocumentEntityConverter converter;

    private Instance<DocumentCollectionManager> manager;

    private DocumentWorkflow workflow;

    private DocumentEventPersistManager persistManager;

    private ClassMappings classMappings;

    private Converters converters;


    @Inject
    DefaultDocumentTemplate(DocumentEntityConverter converter, Instance<DocumentCollectionManager> manager,
                            DocumentWorkflow workflow, DocumentEventPersistManager persistManager,
                            ClassMappings classMappings, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.workflow = workflow;
        this.persistManager = persistManager;
        this.classMappings = classMappings;
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
    protected ClassMappings getClassMappings() {
        return classMappings;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }
}
