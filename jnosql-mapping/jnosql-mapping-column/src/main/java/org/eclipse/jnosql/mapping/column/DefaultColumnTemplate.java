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
package org.eclipse.jnosql.mapping.column;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.column.ColumnManager;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

/**
 * The default implementation of {@link JNoSQLColumnTemplate}
 */
@ApplicationScoped
class DefaultColumnTemplate extends AbstractColumnTemplate {

    private ColumnEntityConverter converter;

    private Instance<ColumnManager> manager;

    private ColumnWorkflow flow;

    private ColumnEventPersistManager eventManager;

    private EntitiesMetadata entities;

    private Converters converters;

    @Inject
    DefaultColumnTemplate(ColumnEntityConverter converter, Instance<ColumnManager> manager,
                          ColumnWorkflow flow,
                          ColumnEventPersistManager eventManager,
                          EntitiesMetadata entities, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.flow = flow;
        this.eventManager = eventManager;
        this.entities = entities;
        this.converters = converters;
    }

    DefaultColumnTemplate() {
    }


    @Override
    protected ColumnEntityConverter getConverter() {
        return converter;
    }

    @Override
    protected ColumnManager getManager() {
        return manager.get();
    }

    @Override
    protected ColumnWorkflow getFlow() {
        return flow;
    }

    @Override
    protected ColumnEventPersistManager getEventManager() {
        return eventManager;
    }

    @Override
    protected EntitiesMetadata getEntities() {
        return entities;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

}
