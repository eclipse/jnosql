/*
 *  Copyright (c) 2020 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.mapping.document.reactive.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplate;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepository;
import org.eclipse.jnosql.mapping.reflection.ClassMapping;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;

import java.lang.reflect.ParameterizedType;

class ReactiveDocumentRepositoryProxy<T> extends AbstractReactiveDocumentRepositoryProxy<T> {


    private final ClassMapping classMapping;
    private final ReactiveRepository<?, ?> repository;
    private final Converters converters;
    private final DocumentTemplate template;

    ReactiveDocumentRepositoryProxy(ReactiveDocumentTemplate reactiveTemplate,
                                    DocumentTemplate template,
                                    Converters converters,
                                    ClassMappings classMappings,
                                    Class<T> repositoryType) {

        Class<T> typeClass = (Class<T>) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.classMapping = classMappings.get(typeClass);
        this.repository = new DefaultReactiveDocumentRepository<>(reactiveTemplate, classMapping);
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
    protected DocumentTemplate getTemplate() {
        return template;
    }
}
