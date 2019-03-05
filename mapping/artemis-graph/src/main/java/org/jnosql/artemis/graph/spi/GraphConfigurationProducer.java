/*
 *  Copyright (c) 2018 Otávio Santana and others
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
import org.jnosql.artemis.ConfigurationReader;
import org.jnosql.artemis.ConfigurationSettingsUnit;
import org.jnosql.artemis.ConfigurationUnit;
import org.jnosql.artemis.graph.GraphProducer;
import org.jnosql.artemis.reflection.Reflections;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;

import static org.jnosql.artemis.util.ConfigurationUnitUtils.getConfigurationUnit;

@ApplicationScoped
class GraphConfigurationProducer {

    @Inject
    private Reflections reflections;

    @Inject
    private Instance<ConfigurationReader> configurationReader;

    @ConfigurationUnit
    @Produces
    public Graph get(InjectionPoint injectionPoint) {
        return getGraphImpl(injectionPoint);
    }

    private Graph getGraphImpl(InjectionPoint injectionPoint) {

        ConfigurationUnit annotation = getConfigurationUnit(injectionPoint);
        ConfigurationSettingsUnit unit = configurationReader.get().read(annotation, GraphProducer.class);
        Class<GraphProducer> configurationClass = unit.<GraphProducer>getProvider()
                .orElseThrow(() -> new IllegalStateException("The GraphProducer provider is required in the configuration"));

        GraphProducer factory = reflections.newInstance(configurationClass);

        return factory.apply(unit.getSettings());
    }
}
