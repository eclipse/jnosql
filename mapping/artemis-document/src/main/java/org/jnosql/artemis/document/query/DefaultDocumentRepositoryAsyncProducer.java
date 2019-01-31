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

import org.jnosql.artemis.Converters;
import org.jnosql.artemis.RepositoryAsync;
import org.jnosql.artemis.document.DocumentRepositoryAsyncProducer;
import org.jnosql.artemis.document.DocumentTemplateAsync;
import org.jnosql.artemis.document.DocumentTemplateAsyncProducer;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;

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
    public <E, ID, T extends RepositoryAsync<E, ID>> T get(Class<T> repositoryClass, DocumentCollectionManagerAsync manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        DocumentTemplateAsync template = producer.get(manager);
        return get(repositoryClass, template);
    }

    @Override
    public <E, ID, T extends RepositoryAsync<E, ID>> T get(Class<T> repositoryClass, DocumentTemplateAsync template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        DocumentRepositoryAsyncProxy<T> handler = new DocumentRepositoryAsyncProxy<>(template,
                classMappings, repositoryClass, converters);
        return (T) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
