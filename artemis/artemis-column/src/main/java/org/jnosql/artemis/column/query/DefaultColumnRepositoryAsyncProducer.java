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

import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.RepositoryAsync;
import jakarta.nosql.mapping.column.ColumnRepositoryAsyncProducer;
import jakarta.nosql.mapping.column.ColumnTemplateAsync;
import jakarta.nosql.mapping.column.ColumnTemplateAsyncProducer;
import jakarta.nosql.mapping.reflection.ClassMappings;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class DefaultColumnRepositoryAsyncProducer implements ColumnRepositoryAsyncProducer {

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;
    @Inject
    private ColumnTemplateAsyncProducer producer;

    @Override
    public <T, K, R extends RepositoryAsync<T, K>> R get(Class<R> repositoryClass, ColumnFamilyManagerAsync manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        ColumnTemplateAsync template = producer.get(manager);
        return get(repositoryClass, template);
    }

    @Override
    public <T, K, R extends RepositoryAsync<T, K>> R get(Class<R> repositoryClass, ColumnTemplateAsync template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        ColumnRepositoryAsyncProxy<R> handler = new ColumnRepositoryAsyncProxy<>(template,
                classMappings, repositoryClass, converters);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
