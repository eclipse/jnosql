/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.column.spi;

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.artemis.column.ColumnRepositoryAsyncProducer;
import org.jnosql.artemis.column.ColumnRepositoryAsyncSupplier;
import org.jnosql.artemis.column.ColumnRepositoryProducer;
import org.jnosql.artemis.column.ColumnRepositorySupplier;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateAsync;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;

@ApplicationScoped
class ColumnRepositoryConfigurationProducer {

    @Inject
    private ColumnRepositoryProducer producer;

    @Inject
    private ColumnRepositoryAsyncProducer producerAsync;

    @Inject
    private TemplateConfigurationProducer configurationProducer;

    @ConfigurationUnit
    @Produces
    public <K, V, R extends Repository<?,?>, E extends Repository<K, V>> ColumnRepositorySupplier<R> get(InjectionPoint injectionPoint) {
        ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        Class<E> repository = (Class) type.getActualTypeArguments()[0];
        ColumnTemplate template = configurationProducer.getTemplate(injectionPoint);
        return () -> (R) producer.get(repository, template);
    }

    @ConfigurationUnit
    @Produces
    public <K, V, R extends RepositoryAsync<?,?>, E extends RepositoryAsync<K, V>> ColumnRepositoryAsyncSupplier<R> getAsync(InjectionPoint injectionPoint) {
        ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        Class<E> repository = (Class) type.getActualTypeArguments()[0];
        ColumnTemplateAsync template = configurationProducer.getTemplateAsync(injectionPoint);
        return () -> (R) producerAsync.get(repository, template);
    }

}
