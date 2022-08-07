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
package org.eclipse.jnosql.mapping.document.query;


import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import java.lang.reflect.ParameterizedType;


/**
 * Proxy handle to generate {@link jakarta.nosql.mapping.Repository}
 *
 * @param <T> the type
 */
class DocumentRepositoryProxy<T> extends AbstractDocumentRepositoryProxy<T> {

    private final DocumentTemplate template;

    private final DocumentRepository repository;

    private final EntityMetadata entityMetadata;

    private final Converters converters;


    DocumentRepositoryProxy(DocumentTemplate template, EntitiesMetadata entities,
                            Class<?> repositoryType, Converters converters) {
        this.template = template;
        Class<T> typeClass = (Class) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.entityMetadata = entities.get(typeClass);
        this.repository = new DocumentRepository(template, entityMetadata);
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
    protected EntityMetadata getEntityMetadata() {
        return entityMetadata;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }


    static class DocumentRepository extends AbstractDocumentRepository implements Repository {

        private final DocumentTemplate template;

        private final EntityMetadata entityMetadata;

        DocumentRepository(DocumentTemplate template, EntityMetadata entityMetadata) {
            this.template = template;
            this.entityMetadata = entityMetadata;
        }

        @Override
        protected DocumentTemplate getTemplate() {
            return template;
        }

        @Override
        protected EntityMetadata getEntityMetadata() {
            return entityMetadata;
        }


    }
}
