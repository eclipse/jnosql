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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentRepositoryProducer;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.document.DocumentTemplateProducer;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class DefaultDocumentRepositoryProducer implements DocumentRepositoryProducer {

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    @Inject
    private DocumentTemplateProducer producer;

    @Override
    public <T, K, R extends Repository<T, K>> R get(Class<R> repositoryClass, DocumentCollectionManager manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        DocumentTemplate template = producer.get(manager);
        return get(repositoryClass, template);
    }

    @Override
    public <T, K, R extends Repository<T, K>> R get(Class<R> repositoryClass, DocumentTemplate template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        DocumentRepositoryProxy<R> handler = new DocumentRepositoryProxy<>(template,
                entities, repositoryClass, converters);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
