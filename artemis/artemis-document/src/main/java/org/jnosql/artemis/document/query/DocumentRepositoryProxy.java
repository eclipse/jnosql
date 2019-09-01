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
package org.jnosql.artemis.document.query;


import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.reflection.ClassMapping;
import jakarta.nosql.mapping.reflection.ClassMappings;

import java.lang.reflect.ParameterizedType;


/**
 * Proxy handle to generate {@link jakarta.nosql.mapping.Repository}
 *
 * @param <T> the type
 */
class DocumentRepositoryProxy<T> extends AbstractDocumentRepositoryProxy<T> {

    private final DocumentTemplate template;

    private final DocumentRepository repository;

    private final ClassMapping classMapping;

    private final Converters converters;


    DocumentRepositoryProxy(DocumentTemplate template, ClassMappings classMappings,
                            Class<?> repositoryType, Converters converters) {
        this.template = template;
        Class<T> typeClass = (Class) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.classMapping = classMappings.get(typeClass);
        this.repository = new DocumentRepository(template, classMapping);
        this.converters = converters;
    }


    @Override
    protected Repository getRepository() {
        return repository;
    }

    @Override
    protected DocumentTemplate getTemplate() {
        return template;
    }

    @Override
    protected ClassMapping getClassMapping() {
        return classMapping;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }


    static class DocumentRepository extends AbstractDocumentRepository implements Repository {

        private final DocumentTemplate template;

        private final ClassMapping classMapping;

        DocumentRepository(DocumentTemplate template, ClassMapping classMapping) {
            this.template = template;
            this.classMapping = classMapping;
        }

        @Override
        protected DocumentTemplate getTemplate() {
            return template;
        }

        @Override
        protected ClassMapping getClassMapping() {
            return classMapping;
        }


    }
}
