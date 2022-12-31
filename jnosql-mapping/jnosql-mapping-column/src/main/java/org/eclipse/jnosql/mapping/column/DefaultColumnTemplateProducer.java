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


import jakarta.nosql.column.ColumnManager;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.column.ColumnTemplateProducer;
import jakarta.nosql.mapping.column.ColumnWorkflow;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import java.util.Objects;

/**
 * The default implementation of {@link ColumnTemplateProducer}
 */
@ApplicationScoped
class DefaultColumnTemplateProducer implements ColumnTemplateProducer {


    @Inject
    private ColumnEntityConverter converter;

    @Inject
    private ColumnWorkflow columnWorkflow;

    @Inject
    private ColumnEventPersistManager eventManager;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    @Override
    public ColumnTemplate get(ColumnManager manager) {
        Objects.requireNonNull(manager, "manager is required");
        return new ProducerColumnTemplate(converter, columnWorkflow, manager,
                eventManager, entities, converters);
    }


    @Vetoed
    static class ProducerColumnTemplate extends AbstractColumnTemplate {

        private ColumnEntityConverter converter;

        private ColumnWorkflow columnWorkflow;

        private ColumnManager manager;

        private ColumnEventPersistManager eventManager;

        private EntitiesMetadata entities;

        private Converters converters;

        ProducerColumnTemplate(ColumnEntityConverter converter, ColumnWorkflow columnWorkflow,
                               ColumnManager manager,
                               ColumnEventPersistManager eventManager,
                               EntitiesMetadata entities,
                               Converters converters) {
            this.converter = converter;
            this.columnWorkflow = columnWorkflow;
            this.manager = manager;
            this.eventManager = eventManager;
            this.entities = entities;
            this.converters = converters;
        }

        ProducerColumnTemplate() {
        }

        @Override
        protected ColumnEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected ColumnManager getManager() {
            return manager;
        }

        @Override
        protected ColumnWorkflow getFlow() {
            return columnWorkflow;
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
}
