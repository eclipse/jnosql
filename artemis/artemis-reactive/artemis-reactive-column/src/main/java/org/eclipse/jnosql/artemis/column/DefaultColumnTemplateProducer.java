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
package org.eclipse.jnosql.artemis.column;


import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.mapping.column.ColumnTemplate;
import jakarta.nosql.mapping.column.ColumnTemplateProducer;
import jakarta.nosql.mapping.column.ColumnWorkflow;
import jakarta.nosql.mapping.reflection.ClassMappings;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Vetoed;
import javax.inject.Inject;
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
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    @Override
    public ColumnTemplate get(ColumnFamilyManager columnFamilyManager) {
        Objects.requireNonNull(columnFamilyManager, "columnFamilyManager is required");
        return new ProducerColumnTemplate(converter, columnWorkflow, columnFamilyManager,
                eventManager, classMappings, converters);
    }


    @Vetoed
    static class ProducerColumnTemplate extends AbstractColumnTemplate {

        private ColumnEntityConverter converter;

        private ColumnWorkflow columnWorkflow;

        private ColumnFamilyManager columnFamilyManager;

        private ColumnEventPersistManager eventManager;

        private ClassMappings classMappings;

        private Converters converters;

        ProducerColumnTemplate(ColumnEntityConverter converter, ColumnWorkflow columnWorkflow,
                               ColumnFamilyManager columnFamilyManager,
                               ColumnEventPersistManager eventManager,
                               ClassMappings classMappings,
                               Converters converters) {
            this.converter = converter;
            this.columnWorkflow = columnWorkflow;
            this.columnFamilyManager = columnFamilyManager;
            this.eventManager = eventManager;
            this.classMappings = classMappings;
            this.converters = converters;
        }

        ProducerColumnTemplate() {
        }

        @Override
        protected ColumnEntityConverter getConverter() {
            return converter;
        }

        @Override
        protected ColumnFamilyManager getManager() {
            return columnFamilyManager;
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
        protected ClassMappings getClassMappings() {
            return classMappings;
        }

        @Override
        protected Converters getConverters() {
            return converters;
        }
    }
}
