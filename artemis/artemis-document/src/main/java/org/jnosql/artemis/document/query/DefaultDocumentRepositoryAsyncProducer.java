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
package org.jnosql.artemis.document.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.RepositoryAsync;
import org.jnosql.artemis.document.DocumentRepositoryAsyncProducer;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.artemis.document.DocumentTemplateAsyncProducer;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.jnosql.diana.document.DocumentCollectionManagerAsync;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class DefaultDocumentRepositoryAsyncProducer implements DocumentRepositoryAsyncProducer {

    @Inject
    private ClassMappings classMappings;
    @Inject
    private Converters converters;
    @Inject
    private DocumentTemplateAsyncProducer producer;

    @Override
    public <T, K, R extends RepositoryAsync<T, K>> R get(Class<R> repositoryClass, DocumentCollectionManagerAsync manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        DocumentTemplateAsync template = producer.get(manager);
        return get(repositoryClass, template);
    }

    @Override
    public <T, K, R extends RepositoryAsync<T, K>> R get(Class<R> repositoryClass, DocumentTemplateAsync template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        DocumentRepositoryAsyncProxy<R> handler = new DocumentRepositoryAsyncProxy<>(template,
                classMappings, repositoryClass, converters);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
