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
package org.eclipse.jnosql.artemis.graph.query;

import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.Repository;
import jakarta.nosql.mapping.reflection.ClassMappings;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.artemis.graph.GraphConverter;
import org.eclipse.jnosql.artemis.graph.GraphRepositoryProducer;
import org.eclipse.jnosql.artemis.graph.GraphTemplate;
import org.eclipse.jnosql.artemis.graph.GraphTemplateProducer;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.lang.reflect.Proxy;
import java.util.Objects;

@ApplicationScoped
class DefaultGraphRepositoryProducer implements GraphRepositoryProducer {

    @Inject
    private ClassMappings classMappings;

    @Inject
    private GraphConverter converter;

    @Inject
    private GraphTemplateProducer producer;

    @Inject
    private Converters converters;

    @Override
    public <T, K, R extends Repository<T, K>> R get(Class<R> repositoryClass, Graph manager) {
        Objects.requireNonNull(repositoryClass, "repository class is required");
        Objects.requireNonNull(manager, "manager class is required");
        GraphTemplate template = producer.get(manager);
        GraphRepositoryProxy<R, K> handler = new GraphRepositoryProxy(template,
                classMappings, repositoryClass, manager, converter, converters);
        return (R) Proxy.newProxyInstance(repositoryClass.getClassLoader(),
                new Class[]{repositoryClass},
                handler);
    }

}
