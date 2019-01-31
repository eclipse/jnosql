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


import org.jnosql.artemis.Converters;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.ClassMappings;

import java.lang.reflect.ParameterizedType;


/**
 * Proxy handle to generate {@link org.jnosql.artemis.Repository}
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
        Class<T> typeClass = Class.class.cast(ParameterizedType.class.cast(repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0]);
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


    class DocumentRepository extends AbstractDocumentRepository implements Repository {

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
