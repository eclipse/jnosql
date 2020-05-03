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
package org.eclipse.jnosql.artemis.document.reactive.query;

import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.document.DocumentRepositoryProducer;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.eclipse.jnosql.artemis.document.reactive.ReactiveDocumentTemplate;
import org.eclipse.jnosql.artemis.document.reactive.ReactiveDocumentTemplateProducer;
import org.eclipse.jnosql.artemis.reactive.ReactiveRepository;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

class DefaultReactiveDocumentRepositoryProducer implements ReactiveDocumentRepositoryProducer {

    @Inject
    private ReactiveDocumentTemplateProducer producerReactive;

    @Inject
    private DocumentRepositoryProducer producer;

    @Inject
    private ClassMappings classMappings;

    @Override
    public <T, K, R extends ReactiveRepository<T, K>> R get(Class<R> repositoryClass,Class<T> type, DocumentTemplate template) {
        
        Objects.requireNonNull(template, "template is required");
        Objects.requireNonNull(repositoryClass, "repositoryClass is required");

        final ReactiveDocumentTemplate reactiveTemplate = producerReactive.get(template);
        producer.get(type, template);
        ReactiveDocumentRepositoryProxy<R> handler = new ReactiveDocumentRepositoryProxy<>(reactiveTemplate,
                template, repository, repositoryClass, classMappings);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
