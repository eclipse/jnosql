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
package org.eclipse.jnosql.artemis.keyvalue.query;


import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.keyvalue.KeyValueTemplate;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class KeyValueRepositoryProxy<T> extends AbstractKeyValueRepositoryProxy<T> {

    private final DefaultKeyValueRepository repository;
    private final KeyValueTemplate template;
    private final Class<T> entityClass;

    KeyValueRepositoryProxy(Class<?> repositoryType, KeyValueTemplate template) {
        Class<T> typeClass = (Class) ((ParameterizedType) repositoryType.getGenericInterfaces()[0])
                .getActualTypeArguments()[0];
        this.repository = new DefaultKeyValueRepository(typeClass, template);
        this.template = template;
        this.entityClass = typeClass;
    }

    @Override
    protected Repository getRepository() {
        return repository;
    }

    @Override
    protected KeyValueTemplate getTemplate() {
        return template;
    }

    @Override
    protected Class getEntityClass() {
        return entityClass;
    }


}
