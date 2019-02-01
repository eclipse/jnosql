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
package org.jnosql.artemis.document.spi;

import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.artemis.document.DocumentRepositoryAsyncProducer;
import org.jnosql.artemis.document.DocumentRepositoryAsyncSupplier;
import org.jnosql.artemis.document.DocumentRepositoryProducer;
import org.jnosql.artemis.document.DocumentRepositorySupplier;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.document.DocumentTemplateAsync;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;

@ApplicationScoped
class DocumentRepositoryConfigurationProducer {

    @Inject
    private DocumentRepositoryProducer producer;

    @Inject
    private DocumentRepositoryAsyncProducer producerAsync;

    @Inject
    private TemplateConfigurationProducer configurationProducer;

    @ConfigurationUnit
    @Produces
    public <K, V, R extends Repository<?,?>, E extends Repository<K, V>> DocumentRepositorySupplier<R> get(InjectionPoint injectionPoint) {
        ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        Class<E> repository = (Class) type.getActualTypeArguments()[0];
        DocumentTemplate template = configurationProducer.get(injectionPoint);
        return () -> (R) producer.get(repository, template);
    }

    @ConfigurationUnit
    @Produces
    public <K, V, R extends RepositoryAsync<?,?>, E extends RepositoryAsync<K, V>> DocumentRepositoryAsyncSupplier<R> getAsync(InjectionPoint injectionPoint) {
        ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        Class<E> repository = (Class) type.getActualTypeArguments()[0];
        DocumentTemplateAsync template = configurationProducer.getAsync(injectionPoint);
        return () -> (R) producerAsync.get(repository, template);
    }
}
