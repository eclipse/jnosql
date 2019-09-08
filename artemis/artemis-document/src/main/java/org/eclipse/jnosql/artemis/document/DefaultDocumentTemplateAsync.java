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
import jakarta.nosql.mapping.reflection.ClassMappings;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * The default implementation of {@link jakarta.nosql.mapping.document.DocumentTemplate}
 */
@SuppressWarnings("unchecked")
class DefaultDocumentTemplateAsync extends AbstractDocumentTemplateAsync {


    private DocumentEntityConverter converter;

    private Instance<DocumentCollectionManagerAsync> manager;

    private ClassMappings classMappings;

    private Converters converters;

    @Inject
    DefaultDocumentTemplateAsync(DocumentEntityConverter converter, Instance<DocumentCollectionManagerAsync> manager,
                                 ClassMappings classMappings, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.classMappings = classMappings;
        this.converters = converters;
    }

    DefaultDocumentTemplateAsync() {
    }

    @Override
    protected DocumentEntityConverter getConverter() {
        return converter;
    }

    @Override
    protected DocumentCollectionManagerAsync getManager() {
        return manager.get();
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
