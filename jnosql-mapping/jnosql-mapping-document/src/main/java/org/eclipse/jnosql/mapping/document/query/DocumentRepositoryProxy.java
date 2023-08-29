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
package org.eclipse.jnosql.mapping.document.query;


import jakarta.data.repository.PageableRepository;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;

import java.lang.reflect.ParameterizedType;


/**
 * Proxy handle to generate {@link PageableRepository}
 *
 * @param <T> the type
 */
class DocumentRepositoryProxy<T> extends AbstractDocumentRepositoryProxy<T> {

    private final JNoSQLDocumentTemplate template;

    private final DocumentRepository repository;

    private final EntityMetadata entityMetadata;

    private final Converters converters;

    private final Class<?> repositoryType;


    DocumentRepositoryProxy(JNoSQLDocumentTemplate template, EntitiesMetadata entities,
                            Class<?> repositoryType, Converters converters) {
        this.template = template;
        Class<T> typeClass = (Class) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.entityMetadata = entities.get(typeClass);
        this.repository = new DocumentRepository(template, entityMetadata);
        this.converters = converters;
        this.repositoryType = repositoryType;
    }


    @Override
    protected PageableRepository getRepository() {
        return repository;
    }

    @Override
    protected Class<?> repositoryType() {
        return repositoryType;
    }

    @Override
    protected JNoSQLDocumentTemplate getTemplate() {
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


    static class DocumentRepository extends AbstractDocumentRepository implements PageableRepository {

        private final JNoSQLDocumentTemplate template;

        private final EntityMetadata entityMetadata;

        DocumentRepository(JNoSQLDocumentTemplate template, EntityMetadata entityMetadata) {
            this.template = template;
            this.entityMetadata = entityMetadata;
        }

        @Override
        protected JNoSQLDocumentTemplate getTemplate() {
            return template;
        }

        @Override
        protected EntityMetadata getEntityMetadata() {
            return entityMetadata;
        }


    }
}
