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
package org.eclipse.jnosql.mapping.column.query;


import jakarta.data.repository.PageableRepository;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.column.JNoSQLColumnTemplate;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.EntityMetadata;

import java.lang.reflect.ParameterizedType;


/**
 * Proxy handle to generate {@link jakarta.data.repository.PageableRepository}
 *
 * @param <T>  the type
 * @param <K> the K type
 */
class ColumnRepositoryProxy<T, K> extends AbstractColumnRepositoryProxy {


    private final JNoSQLColumnTemplate template;

    private final ColumnRepository repository;

    private final EntityMetadata entityMetadata;

    private final Converters converters;


    ColumnRepositoryProxy(JNoSQLColumnTemplate template, EntitiesMetadata entities, Class<?> repositoryType,
                          Converters converters) {
        this.template = template;
        Class<T> typeClass = (Class) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.entityMetadata = entities.get(typeClass);
        this.repository = new ColumnRepository(template, entityMetadata);
        this.converters = converters;
    }

    @Override
    protected PageableRepository getRepository() {
        return repository;
    }

    @Override
    protected EntityMetadata getEntityMetadata() {
        return entityMetadata;
    }

    @Override
    protected JNoSQLColumnTemplate getTemplate() {
        return template;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }


    static class ColumnRepository extends AbstractColumnRepository implements PageableRepository {

        private final JNoSQLColumnTemplate template;

        private final EntityMetadata entityMetadata;

        ColumnRepository(JNoSQLColumnTemplate template, EntityMetadata entityMetadata) {
            this.template = template;
            this.entityMetadata = entityMetadata;
        }

        @Override
        protected JNoSQLColumnTemplate getTemplate() {
            return template;
        }

        @Override
        protected EntityMetadata getEntityMetadata() {
            return entityMetadata;
        }

    }
}
