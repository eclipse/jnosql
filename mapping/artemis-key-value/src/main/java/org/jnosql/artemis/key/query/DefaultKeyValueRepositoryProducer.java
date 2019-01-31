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
package org.jnosql.artemis.key.query;

import org.jnosql.artemis.Repository;
import org.jnosql.artemis.key.KeyValueRepositoryProducer;
import org.jnosql.artemis.key.KeyValueTemplate;
import org.jnosql.artemis.key.KeyValueTemplateProducer;
import org.jnosql.diana.api.key.BucketManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class DefaultKeyValueRepositoryProducer implements KeyValueRepositoryProducer {

    @Inject
    private KeyValueTemplateProducer producer;

    @Override
    public <E, ID, T extends Repository<E, ID>> T get(Class<T> repositoryClass, BucketManager manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        KeyValueTemplate template = producer.get(manager);
        return get(repositoryClass, template);
    }

    @Override
    public <E, ID, T extends Repository<E, ID>> T get(Class<T> repositoryClass, KeyValueTemplate template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        KeyValueRepositoryProxy<T> handler = new KeyValueRepositoryProxy<>(repositoryClass, template);
        return (T) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
