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
package org.eclipse.jnosql.mapping.graph.configuration;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.CDI;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.communication.Settings;
import org.eclipse.jnosql.mapping.core.config.MicroProfileSettings;
import org.eclipse.jnosql.communication.graph.GraphConfiguration;
import org.eclipse.jnosql.mapping.reflection.Reflections;

import java.util.function.Supplier;

import static org.eclipse.jnosql.mapping.core.config.MappingConfigurations.GRAPH_PROVIDER;


@ApplicationScoped
class GraphSupplier implements Supplier<Graph> {

    @Override
    @Produces
    @ApplicationScoped
    public Graph get(){
        Settings settings = MicroProfileSettings.INSTANCE;

        GraphConfiguration configuration = settings.get(GRAPH_PROVIDER, Class.class)
                .filter(GraphConfiguration.class::isAssignableFrom)
                .map(c -> {
                    final Reflections reflections = CDI.current().select(Reflections.class).get();
                    return (GraphConfiguration) reflections.newInstance(c);
                }).orElseGet(GraphConfiguration::getConfiguration);

        return configuration.apply(settings);
    }

    public void close(@Disposes Graph graph) throws Exception {
        graph.close();
    }
}
