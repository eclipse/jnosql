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
package org.jnosql.artemis.column.query;


import org.jnosql.artemis.Converters;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.ClassMappings;

import java.lang.reflect.ParameterizedType;


/**
 * Proxy handle to generate {@link Repository}
 *
 * @param <T>  the type
 * @param <ID> the ID type
 */
class ColumnRepositoryProxy<T, ID> extends AbstractColumnRepositoryProxy {


    private final ColumnTemplate template;

    private final ColumnRepository repository;

    private final ClassMapping classMapping;

    private final Converters converters;


    ColumnRepositoryProxy(ColumnTemplate template, ClassMappings classMappings, Class<?> repositoryType,
                          Converters converters) {
        this.template = template;
        Class<T> typeClass = Class.class.cast(ParameterizedType.class.cast(repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0]);
        this.classMapping = classMappings.get(typeClass);
        this.repository = new ColumnRepository(template, classMapping);
        this.converters = converters;
    }

    @Override
    protected Repository getRepository() {
        return repository;
    }

    @Override
    protected ClassMapping getClassMapping() {
        return classMapping;
    }

    @Override
    protected ColumnTemplate getTemplate() {
        return template;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }


    class ColumnRepository extends AbstractColumnRepository implements Repository {

        private final ColumnTemplate template;

        private final ClassMapping classMapping;

        ColumnRepository(ColumnTemplate template, ClassMapping classMapping) {
            this.template = template;
            this.classMapping = classMapping;
        }

        @Override
        protected ColumnTemplate getTemplate() {
            return template;
        }

        @Override
        protected ClassMapping getClassMapping() {
            return classMapping;
        }

    }
}
