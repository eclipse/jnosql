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


import org.jnosql.artemis.EntityPostPersit;
import org.jnosql.artemis.EntityPrePersist;
import org.jnosql.diana.column.ColumnDeleteQuery;
import org.jnosql.diana.column.ColumnEntity;
import org.jnosql.diana.column.ColumnQuery;

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
    private Event<EntityPostPersit> entityPostPersitEvent;

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
        columnEntityPrePersistEvent.fire(ColumnEntityPrePersist.of(entity));
    }

    @Override
    public void firePostColumn(ColumnEntity entity) {
        columnEntityPostPersistEvent.fire(ColumnEntityPostPersist.of(entity));
    }

    @Override
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(EntityPrePersist.of(entity));
    }

    @Override
    public <T> void firePostEntity(T entity) {
        entityPostPersitEvent.fire(EntityPostPersit.of(entity));
    }

    @Override
    public <T> void firePreColumnEntity(T entity) {
        entityColumnPrePersist.fire(EntityColumnPrePersist.of(entity));
    }

    @Override
    public <T> void firePostColumnEntity(T entity) {
        entityColumnPostPersist.fire(EntityColumnPostPersist.of(entity));
    }

    @Override
    public void firePreQuery(ColumnQuery query) {
        columnQueryExecute.fire(ColumnQueryExecute.of(query));
    }

    @Override
    public void firePreDeleteQuery(ColumnDeleteQuery query) {
        columnDeleteQueryExecute.fire(ColumnDeleteQueryExecute.of(query));
    }
}
