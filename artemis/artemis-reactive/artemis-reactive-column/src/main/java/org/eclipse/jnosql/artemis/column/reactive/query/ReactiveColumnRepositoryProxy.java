/*
 *  Copyright (c) 2020 Otávio Santana and others
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
package org.eclipse.jnosql.artemis.column.reactive.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnTemplate;
import org.eclipse.jnosql.artemis.reflection.ClassMapping;
import org.eclipse.jnosql.artemis.reflection.ClassMappings;
import org.eclipse.jnosql.artemis.column.reactive.ReactiveColumnTemplate;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;

import java.lang.reflect.ParameterizedType;

class ReactiveColumnRepositoryProxy<T> extends AbstractReactiveColumnRepositoryProxy<T> {


    private final ClassMapping classMapping;
    private final ReactiveRepository<?, ?> repository;
    private final Class<T> entityClass;
    private final Converters converters;
    private final ColumnTemplate template;

    ReactiveColumnRepositoryProxy(ReactiveColumnTemplate reactiveTemplate,
                                  ColumnTemplate template,
                                  Converters converters,
                                  ClassMappings classMappings,
                                  Class<T> repositoryType) {

        Class<T> typeClass = (Class<T>) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.classMapping = classMappings.get(typeClass);
        this.repository = new DefaultReactiveColumnRepository(reactiveTemplate, classMapping);
        this.entityClass = typeClass;
        this.converters = converters;
        this.template = template;
    }


    @Override
    protected ReactiveRepository<?, ?> getRepository() {
        return repository;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

    @Override
    protected ClassMapping getClassMapping() {
        return classMapping;
    }

    @Override
    protected ColumnTemplate getTemplate() {
        return template;
    }
}
