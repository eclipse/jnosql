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
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.document.DocumentRepositoryProducer;
import org.jnosql.artemis.document.DocumentTemplate;
import org.jnosql.artemis.document.DocumentTemplateProducer;
import org.jnosql.artemis.reflection.ClassMappings;
import org.jnosql.diana.api.document.DocumentCollectionManager;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class DefaultDocumentRepositoryProducer implements DocumentRepositoryProducer {

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Inject
    private DocumentTemplateProducer producer;

    @Override
    public <E, ID, T extends Repository<E, ID>> T get(Class<T> repositoryClass, DocumentCollectionManager manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        DocumentTemplate template = producer.get(manager);
        return get(repositoryClass, template);
    }

    @Override
    public <E, ID, T extends Repository<E, ID>> T get(Class<T> repositoryClass, DocumentTemplate template) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(template, "template class is required");

        DocumentRepositoryProxy<T> handler = new DocumentRepositoryProxy<>(template,
                classMappings, repositoryClass, converters);
        return (T) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
