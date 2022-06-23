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
package org.eclipse.jnosql.mapping.document.reactive.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentTemplate;
import org.eclipse.jnosql.mapping.reflection.ClassMappings;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplate;
import org.eclipse.jnosql.mapping.document.reactive.ReactiveDocumentTemplateProducer;
import org.eclipse.jnosql.mapping.reactive.ReactiveRepository;

import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

class DefaultReactiveDocumentRepositoryProducer implements ReactiveDocumentRepositoryProducer {

    @Inject
    private ReactiveDocumentTemplateProducer producerReactive;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Override
    public <T, K, R extends ReactiveRepository<T, K>> R get(Class<R> repositoryClass, DocumentTemplate template) {
        
        Objects.requireNonNull(template, "template is required");
        Objects.requireNonNull(repositoryClass, "repositoryClass is required");

        final ReactiveDocumentTemplate reactiveTemplate = producerReactive.get(template);
        ReactiveDocumentRepositoryProxy<R> handler = new ReactiveDocumentRepositoryProxy<>(reactiveTemplate,
                template, converters, classMappings, repositoryClass);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }
}
