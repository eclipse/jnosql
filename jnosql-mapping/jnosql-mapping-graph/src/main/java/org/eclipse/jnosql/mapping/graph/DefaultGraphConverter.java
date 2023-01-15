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
package org.eclipse.jnosql.mapping.graph;

import org.eclipse.jnosql.mapping.Converters;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
class DefaultGraphConverter extends GraphConverter implements GraphConverter {

    private EntitiesMetadata entities;

    private Converters converters;

    private Instance<Graph> graph;

    private GraphEventPersistManager eventManager;

    @Inject
    DefaultGraphConverter(EntitiesMetadata entities, Converters converters,
                          Instance<Graph> graph, GraphEventPersistManager eventManager) {
        this.entities = entities;
        this.converters = converters;
        this.graph = graph;
        this.eventManager = eventManager;
    }

    DefaultGraphConverter() {
    }

    @Override
    protected EntitiesMetadata getEntities() {
        return entities;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

    @Override
    protected GraphEventPersistManager getEventManager() {
        return eventManager;
    }

    @Override
    protected Graph getGraph() {
        return graph.get();
    }
}
