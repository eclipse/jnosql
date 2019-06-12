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
import jakarta.nosql.mapping.RepositoryAsync;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.artemis.reflection.ClassMapping;
import org.jnosql.artemis.reflection.ClassMappings;

import java.lang.reflect.ParameterizedType;

/**
 * Proxy handle to generate {@link RepositoryAsync}
 *
 * @param <T> the type
 */
class ColumnRepositoryAsyncProxy<T> extends AbstractColumnRepositoryAsyncProxy<T> {

    private final ColumnTemplateAsync template;

    private final ColumnRepositoryAsync repository;

    private final ClassMapping classMapping;

    private final Converters converters;


    ColumnRepositoryAsyncProxy(ColumnTemplateAsync template, ClassMappings classMappings,
                               Class<?> repositoryType, Converters converters) {

        this.template = template;
        Class<T> typeClass = (Class) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.classMapping = classMappings.get(typeClass);
        this.repository = new ColumnRepositoryAsync(template, classMapping);
        this.converters = converters;
    }

    @Override
    protected RepositoryAsync getRepository() {
        return repository;
    }

    @Override
    protected ClassMapping getClassMapping() {
        return classMapping;
    }

    @Override
    protected ColumnTemplateAsync getTemplate() {
        return template;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }


    class ColumnRepositoryAsync extends AbstractColumnRepositoryAsync implements RepositoryAsync {


        private final ColumnTemplateAsync template;


        private final ClassMapping classMapping;

        ColumnRepositoryAsync(ColumnTemplateAsync repository,  ClassMapping classMapping) {
            this.template = repository;
            this.classMapping = classMapping;
        }

        @Override
        protected ColumnTemplateAsync getTemplate() {
            return template;
        }


        @Override
        protected ClassMapping getClassMapping() {
            return classMapping;
        }

    }
}
