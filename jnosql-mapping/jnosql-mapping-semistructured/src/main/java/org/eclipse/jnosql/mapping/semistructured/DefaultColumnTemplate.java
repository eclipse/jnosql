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
package org.eclipse.jnosql.mapping.semistructured;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.semistructured.DatabaseManager;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.Database;
import org.eclipse.jnosql.mapping.DatabaseType;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;

@Default
@Database(DatabaseType.COLUMN)
@ApplicationScoped
class DefaultColumnTemplate extends AbstractColumnTemplate {

    private ColumnEntityConverter converter;

    private Instance<DatabaseManager> manager;

    private ColumnEventPersistManager eventManager;

    private EntitiesMetadata entities;

    private Converters converters;

    @Inject
    DefaultColumnTemplate(ColumnEntityConverter converter, Instance<DatabaseManager> manager,
                          ColumnEventPersistManager eventManager,
                          EntitiesMetadata entities, Converters converters) {
        this.converter = converter;
        this.manager = manager;
        this.eventManager = eventManager;
        this.entities = entities;
        this.converters = converters;
    }

    DefaultColumnTemplate(){
    }

    @Override
    protected ColumnEntityConverter getConverter() {
        return converter;
    }

    @Override
    protected DatabaseManager getManager() {
        return manager.get();
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
