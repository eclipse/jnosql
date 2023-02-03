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


import org.eclipse.jnosql.communication.column.ColumnDeleteQuery;
import org.eclipse.jnosql.communication.column.ColumnEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.mapping.EntityPostPersist;
import org.eclipse.jnosql.mapping.EntityPrePersist;

/**
 * This interface represents the manager of events. When an entity be either saved or updated an event will be fired.
 * This order going to be:
 * 1) firePreEntity
 * 2) firePreColumnEntity
 * 3) firePreColumn
 * 4) firePostColumn
 * 5) firePostEntity
 * 6) firePostColumnEntity
 *
 * @see ColumnWorkflow
 */
@ApplicationScoped
public class ColumnEventPersistManager {


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



    /**
     * Fire an event once the method is called
     *
     * @param entity the entity
     * @param <T>    the entity type
     */
    public <T> void firePreEntity(T entity) {
        entityPrePersistEvent.fire(EntityPrePersist.of(entity));
    }

    /**
     * Fire an event after firePostEntity
     *
     * @param entity the entity
     * @param <T>    the entity kind
     */
    public <T> void firePostEntity(T entity) {
        entityPostPersistEvent.fire(EntityPostPersist.of(entity));
    }

    /**
     * Fire an event before the query is executed
     *
     * @param query the query
     */
    public void firePreQuery(ColumnQuery query) {
        columnQueryExecute.fire(new ColumnQueryExecute(query));
    }

    /**
     * Fire an event before the delete query is executed
     *
     * @param query the query
     */
    public void firePreDeleteQuery(ColumnDeleteQuery query) {
        columnDeleteQueryExecute.fire(new ColumnDeleteQueryExecute(query));
    }
}
