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
package org.eclipse.jnosql.mapping.graph.query;

import jakarta.data.repository.PageableRepository;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.graph.GraphConverter;
import org.eclipse.jnosql.mapping.graph.GraphTemplate;
import org.eclipse.jnosql.mapping.graph.GraphTemplateProducer;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.apache.tinkerpop.gremlin.structure.Graph;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class GraphRepositoryProducer {

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private GraphConverter converter;

    @Inject
    private GraphTemplateProducer producer;

    @Inject
    private Converters converters;

    public <T, K, R extends PageableRepository<T, K>> R get(Class<R> repositoryClass, Graph manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        GraphTemplate template = producer.get(manager);
        GraphRepositoryProxy<R, K> handler = new GraphRepositoryProxy(template,
                entities, repositoryClass, manager, converter, converters);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }

}
