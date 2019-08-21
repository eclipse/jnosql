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
package org.jnosql.artemis.column;

import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.mapping.column.ColumnWorkflow;
import jakarta.nosql.mapping.reflection.ClassMappings;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Optional;

/**
 * The default implementation of {@link jakarta.nosql.mapping.column.ColumnTemplate}
 */
class DefaultColumnTemplate extends AbstractColumnTemplate {

    private ColumnEntityConverter converter;

    private Instance<ColumnFamilyManager> manager;

    private ColumnWorkflow flow;

    private ColumnEventPersistManager eventManager;

    private ClassMappings classMappings;

    private Converters converters;

    @Inject
    DefaultColumnTemplate(ColumnEntityConverter converter, Instance<ColumnFamilyManager> manager,
                          ColumnWorkflow flow,
                          ColumnEventPersistManager eventManager,
                          ClassMappings classMappings, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.flow = flow;
        this.eventManager = eventManager;
        this.classMappings = classMappings;
        this.converters = converters;
    }

    DefaultColumnTemplate() {
    }


    @Override
    protected ColumnEntityConverter getConverter() {
        return converter;
    }

    @Override
    protected ColumnFamilyManager getManager() {
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
    protected ClassMappings getClassMappings() {
        return classMappings;
    }

    @Override
    protected Converters getConverters() {
        return converters;
    }

}
