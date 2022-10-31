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


import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.EntityPostPersist;
import jakarta.nosql.mapping.EntityPrePersist;
import jakarta.nosql.mapping.column.ColumnDeleteQueryExecute;
import jakarta.nosql.mapping.column.ColumnEntityPostPersist;
import jakarta.nosql.mapping.column.ColumnEntityPrePersist;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.mapping.column.ColumnQueryExecute;
import jakarta.nosql.mapping.column.EntityColumnPostPersist;
import jakarta.nosql.mapping.column.EntityColumnPrePersist;
import org.eclipse.jnosql.mapping.DefaultEntityPostPersist;
import org.eclipse.jnosql.mapping.DefaultEntityPrePersist;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.inject.Inject;

/**
 * The default implementation of {@link ColumnEventPersistManager}
 */
@ApplicationScoped
class DefaultColumnEventPersistManager implements ColumnEventPersistManager {

    @Inject
    private Event<ColumnEntityPrePersist> columnEntityPrePersistEvent;

    @Inject
    private Event<ColumnEntityPostPersist> columnEntityPostPersistEvent;

    @Inject
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Inject
    private Event<EntityPostPersist> entityPostPersistEvent;

    @Inject
    private Event<EntityColumnPrePersist> entityColumnPrePersist;

    @Inject
    private Event<EntityColumnPostPersist> entityColumnPostPersist;

    @Inject
    private Event<ColumnQueryExecute> columnQueryExecute;

    @Inject
    private Event<ColumnDeleteQueryExecute> columnDeleteQueryExecute;

    @Override
    public void firePreColumn(ColumnEntity entity) {
        columnEntityPrePersistEvent.fire(new DefaultColumnEntityPrePersist(entity));
    }

    @Override
    public void firePostColumn(ColumnEntity entity) {
        columnEntityPostPersistEvent.fire(new DefaultColumnEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(new DefaultEntityPrePersist(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersistEvent.fire(new DefaultEntityPostPersist(entity));
    }

    @Override
    public <T> void firePreColumnEntity(T entity) {
        entityColumnPrePersist.fire(new DefaultEntityColumnPrePersist(entity));
    }

    @Override
    public <T> void firePostColumnEntity(T entity) {
        entityColumnPostPersist.fire(new DefaultEntityColumnPostPersist(entity));
    }

    @Override
    public void firePreQuery(ColumnQuery query) {
        columnQueryExecute.fire(new DefaultColumnQueryExecute(query));
    }

    @Override
    public void firePreDeleteQuery(ColumnDeleteQuery query) {
        columnDeleteQueryExecute.fire(new DefaultColumnDeleteQueryExecute(query));
    }
}
