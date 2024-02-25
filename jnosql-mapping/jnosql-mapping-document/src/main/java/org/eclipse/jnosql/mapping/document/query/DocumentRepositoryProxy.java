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


import jakarta.data.repository.BasicRepository;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.core.query.AbstractRepository;
import org.eclipse.jnosql.mapping.document.JNoSQLDocumentTemplate;
import org.eclipse.jnosql.mapping.metadata.EntityMetadata;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;


/**
 * Proxy handle to generate {@link BasicRepository} for document-based repositories.
 *
 * @param <T> the type
 * @param <K> the key type
 */
public class DocumentRepositoryProxy<T, K> extends AbstractDocumentRepositoryProxy<T, K> {

    private final JNoSQLDocumentTemplate template;

    private final DocumentRepository<T, K> repository;

    private final EntityMetadata entityMetadata;

    private final Converters converters;

    private final Class<?> repositoryType;


    DocumentRepositoryProxy(JNoSQLDocumentTemplate template, EntitiesMetadata entities,
                            Class<?> repositoryType, Converters converters) {
        this.template = template;
        Class<T> typeClass = (Class<T>) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.entityMetadata = entities.get(typeClass);
        this.repository = new DocumentRepository<>(template, entityMetadata);
        this.converters = converters;
        this.repositoryType = repositoryType;
    }


    @Override
    protected AbstractRepository<T, K> repository() {
        return repository;
    }

    @Override
    protected Class<?> repositoryType() {
        return repositoryType;
    }

    @Override
    protected JNoSQLDocumentTemplate template() {
        return template;
    }

    @Override
    protected EntityMetadata entityMetadata() {
        return entityMetadata;
    }

    @Override
    protected Converters converters() {
        return converters;
    }

    /**
     * DocumentRepository that interacts with the underlying document database.
     *
     * @param <T> the type
     * @param <K> the key type
     */
    public static class DocumentRepository<T, K> extends AbstractDocumentRepository<T, K>  {

        private final JNoSQLDocumentTemplate template;

        private final EntityMetadata entityMetadata;

        DocumentRepository(JNoSQLDocumentTemplate template, EntityMetadata entityMetadata) {
            this.template = template;
            this.entityMetadata = entityMetadata;
        }

        @Override
        protected JNoSQLDocumentTemplate template() {
            return template;
        }

        @Override
        protected EntityMetadata entityMetadata() {
            return entityMetadata;
        }

        /**
         * Creates a new instance of DocumentRepository with the provided JNoSQLDocumentTemplate and EntityMetadata.
         *
         * @param <T>      The type of entities managed by the repository.
         * @param <K>      The type of the key used for document-based operations.
         * @param template The JNoSQLDocumentTemplate used for document database operations. Must not be {@code null}.
         * @param metadata The metadata information about the entity. Must not be {@code null}.
         * @return A new instance of DocumentRepository.
         * @throws NullPointerException If either the template or metadata is {@code null}.
         */
        public static <T, K> DocumentRepository<T, K> of(JNoSQLDocumentTemplate template, EntityMetadata metadata) {
            Objects.requireNonNull(template,"template is required");
            Objects.requireNonNull(metadata,"metadata is required");
            return new DocumentRepository<>(template, metadata);
        }

    }
}
