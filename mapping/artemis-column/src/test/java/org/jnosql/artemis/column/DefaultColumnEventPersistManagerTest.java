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
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.enterprise.event.Event;

import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.delete;
import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DefaultColumnEventPersistManagerTest {


    @InjectMocks
    private DefaultColumnEventPersistManager subject;

    @Mock
    private Event<ColumnEntityPrePersist> columnEntityPrePersistEvent;

    @Mock
    private Event<ColumnEntityPostPersist> columnEntityPostPersistEvent;

    @Mock
    private Event<EntityPrePersist> entityPrePersistEvent;

    @Mock
    private Event<EntityPostPersit> entityPostPersitEvent;

    @Mock
    private Event<EntityColumnPrePersist> entityColumnPrePersist;

    @Mock
    private Event<EntityColumnPostPersist> entityColumnPostPersist;

    @Mock
    private Event<ColumnQueryExecute> columnQueryExecute;

    @Mock
    private Event<ColumnDeleteQueryExecute> columnDeleteQueryExecute;


    @Test
    public void shouldFirePreColumn() {
        ColumnEntity entity = ColumnEntity.of("columnFamily");
        subject.firePreColumn(entity);
        ArgumentCaptor<ColumnEntityPrePersist> captor = ArgumentCaptor.forClass(ColumnEntityPrePersist.class);
        verify(columnEntityPrePersistEvent).fire(captor.capture());

        ColumnEntityPrePersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }


    @Test
    public void shouldFirePostColumn() {
        ColumnEntity entity = ColumnEntity.of("columnFamily");
        subject.firePostColumn(entity);
        ArgumentCaptor<ColumnEntityPostPersist> captor = ArgumentCaptor.forClass(ColumnEntityPostPersist.class);
        verify(columnEntityPostPersistEvent).fire(captor.capture());

        ColumnEntityPostPersist captorValue = captor.getValue();
        assertEquals(entity, captorValue.getEntity());
    }

    @Test
    public void shouldFirePreEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePreEntity(jedi);
        ArgumentCaptor<EntityPrePersist> captor = ArgumentCaptor.forClass(EntityPrePersist.class);
        verify(entityPrePersistEvent).fire(captor.capture());
        EntityPrePersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePostEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePostEntity(jedi);
        ArgumentCaptor<EntityPostPersit> captor = ArgumentCaptor.forClass(EntityPostPersit.class);
        verify(entityPostPersitEvent).fire(captor.capture());
        EntityPostPersit value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePreColumnEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePreColumnEntity(jedi);
        ArgumentCaptor<EntityColumnPrePersist> captor = ArgumentCaptor.forClass(EntityColumnPrePersist.class);
        verify(entityColumnPrePersist).fire(captor.capture());
        EntityColumnPrePersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePostColumnEntity() {
        Jedi jedi = new Jedi();
        jedi.name = "Luke";
        subject.firePostColumnEntity(jedi);
        ArgumentCaptor<EntityColumnPostPersist> captor = ArgumentCaptor.forClass(EntityColumnPostPersist.class);
        verify(entityColumnPostPersist).fire(captor.capture());
        EntityColumnPostPersist value = captor.getValue();
        assertEquals(jedi, value.getValue());
    }

    @Test
    public void shouldFirePreQuery() {


        ColumnQuery query = select().from("person").build();
        subject.firePreQuery(query);
        ArgumentCaptor<ColumnQueryExecute> captor = ArgumentCaptor.forClass(ColumnQueryExecute.class);
        verify(columnQueryExecute).fire(captor.capture());
        assertEquals(query, captor.getValue().getQuery());
    }

    @Test
    public void shouldFirePreDeleteQuery() {

        ColumnDeleteQuery query = delete().from("person").build();
        subject.firePreDeleteQuery(query);
        ArgumentCaptor<ColumnDeleteQueryExecute> captor = ArgumentCaptor.forClass(ColumnDeleteQueryExecute.class);
        verify(columnDeleteQueryExecute).fire(captor.capture());
        assertEquals(query, captor.getValue().getQuery());
    }


    class Jedi {
        private String name;
    }
}