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
package org.jnosql.artemis.graph.spi;

import org.apache.tinkerpop.gremlin.structure.Graph;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.Repository;
import org.jnosql.artemis.graph.GraphRepositoryProducer;
import org.jnosql.artemis.graph.GraphRepositorySupplier;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import java.lang.reflect.ParameterizedType;

@ApplicationScoped
class GraphRepositoryConfigurationProducer {

    @Inject
    private GraphRepositoryProducer producer;

    @Inject
    private GraphConfigurationProducer configurationProducer;

    @ConfigurationUnit
    @Produces
    public <K, V, R extends Repository<?,?>, E extends Repository<K, V>> GraphRepositorySupplier<R> get(InjectionPoint injectionPoint) {
        ParameterizedType type = (ParameterizedType) injectionPoint.getType();
        Class<E> repository = (Class) type.getActualTypeArguments()[0];
        Graph graph = configurationProducer.get(injectionPoint);
        return () -> (R) producer.get(repository, graph);
    }

}
