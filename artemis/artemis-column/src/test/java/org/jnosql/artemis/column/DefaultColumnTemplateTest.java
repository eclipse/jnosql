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

import org.jnosql.artemis.CDIExtension;
import jakarta.nosql.mapping.Converters;
import org.jnosql.artemis.IdNotFoundException;
import jakarta.nosql.mapping.PreparedStatement;
import org.jnosql.artemis.model.Job;
import org.jnosql.artemis.model.Movie;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.reflection.ClassMappings;
import static jakarta.nosql.NonUniqueResultException;
import org.jnosql.diana.column.Column;
import org.jnosql.diana.column.ColumnCondition;
import org.jnosql.diana.column.ColumnDeleteQuery;
import org.jnosql.diana.column.ColumnEntity;
import org.jnosql.diana.column.ColumnFamilyManager;
import org.jnosql.diana.column.ColumnQuery;
import org.jnosql.diana.column.query.ColumnQueryBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.jnosql.diana.column.query.ColumnQueryBuilder.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(CDIExtension.class)
public class DefaultColumnTemplateTest {

    private Person person = Person.builder().
            withAge().
            withPhones(Arrays.asList("234", "432")).
            withName("Name")
            .withId(19)
            .withIgnore().build();

    private Column[] columns = new Column[]{
            Column.of("age", 10),
            Column.of("phones", Arrays.asList("234", "432")),
            Column.of("name", "Name"),
            Column.of("id", 19L),
    };


    @Inject
    private ColumnEntityConverter converter;

    @Inject
    private ClassMappings classMappings;

    @Inject
    private Converters converters;

    private ColumnFamilyManager managerMock;

    private DefaultColumnTemplate subject;

    private ArgumentCaptor<ColumnEntity> captor;

    private ColumnEventPersistManager columnEventPersistManager;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(ColumnFamilyManager.class);
        columnEventPersistManager = Mockito.mock(ColumnEventPersistManager.class);
        captor = ArgumentCaptor.forClass(ColumnEntity.class);
        Instance<ColumnFamilyManager> instance = Mockito.mock(Instance.class);
        Mockito.when(instance.get()).thenReturn(managerMock);
        this.subject = new DefaultColumnTemplate(converter, instance, new DefaultColumnWorkflow(columnEventPersistManager, converter),
                columnEventPersistManager, classMappings, converters);
    }

    @Test
    public void shouldInsert() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        subject.insert(this.person);
        verify(managerMock).insert(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        verify(columnEventPersistManager).firePreColumn(any(ColumnEntity.class));
        verify(columnEventPersistManager).firePostColumn(any(ColumnEntity.class));
        ColumnEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getColumns().size());
    }


    @Test
    public void shouldMergeOnInsert() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        Person person = Person.builder().build();
        Person result = subject.insert(person);
        verify(managerMock).insert(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        verify(columnEventPersistManager).firePreColumn(any(ColumnEntity.class));
        verify(columnEventPersistManager).firePostColumn(any(ColumnEntity.class));
       assertTrue(person == result);
       assertEquals(10, person.getAge());

    }




    @Test
    public void shouldInsertTTL() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class),
                        any(Duration.class)))
                .thenReturn(columnEntity);

        subject.insert(this.person, Duration.ofHours(2));
        verify(managerMock).insert(captor.capture(), Mockito.eq(Duration.ofHours(2)));
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        verify(columnEventPersistManager).firePreColumn(any(ColumnEntity.class));
        verify(columnEventPersistManager).firePostColumn(any(ColumnEntity.class));
        ColumnEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getColumns().size());
    }

    @Test
    public void shouldUpdate() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        subject.update(this.person);
        verify(managerMock).update(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        verify(columnEventPersistManager).firePreColumn(any(ColumnEntity.class));
        verify(columnEventPersistManager).firePostColumn(any(ColumnEntity.class));
        ColumnEntity value = captor.getValue();
        assertEquals("Person", value.getName());
        assertEquals(4, value.getColumns().size());
    }

    @Test
    public void shouldMergeOnUpdate() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        Person person = Person.builder().build();
        Person result = subject.update(person);
        verify(managerMock).update(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        verify(columnEventPersistManager).firePreColumn(any(ColumnEntity.class));
        verify(columnEventPersistManager).firePostColumn(any(ColumnEntity.class));
        assertTrue(person == result);
        assertEquals(10, person.getAge());

    }



    @Test
    public void shouldInsertEntitiesTTL() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));
        Duration duration = Duration.ofHours(2);

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class), Mockito.eq(duration)))
                .thenReturn(columnEntity);

        subject.insert(Arrays.asList(person, person), duration);
        verify(managerMock, times(2)).insert(any(ColumnEntity.class), any(Duration.class));
    }

    @Test
    public void shouldInsertEntities() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        subject.insert(Arrays.asList(person, person));
        verify(managerMock, times(2)).insert(any(ColumnEntity.class));
    }

    @Test
    public void shouldUpdateEntities() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        subject.update(Arrays.asList(person, person));
        verify(managerMock, times(2)).update(any(ColumnEntity.class));
    }

    @Test
    public void shouldDelete() {

        ColumnDeleteQuery query = ColumnQueryBuilder.delete().from("delete").build();
        subject.delete(query);
        verify(managerMock).delete(query);
    }

    @Test
    public void shouldSelect() {
        ColumnQuery query = select().from("person").build();
        subject.select(query);
        verify(managerMock).select(query);
    }

    @Test
    public void shouldReturnSingleResult() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .select(any(ColumnQuery.class)))
                .thenReturn(singletonList(columnEntity));

        ColumnQuery query = select().from("person").build();

        Optional<Person> result = subject.singleResult(query);
        assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnSingleResultIsEmpty() {
        Mockito.when(managerMock
                .select(any(ColumnQuery.class)))
                .thenReturn(emptyList());

        ColumnQuery query = select().from("person").build();

        Optional<Person> result = subject.singleResult(query);
        assertFalse(result.isPresent());
    }

    @Test
    public void shouldReturnErrorWhenThereMoreThanASingleResult() {
        Assertions.assertThrows(NonUniqueResultException.class, () -> {
            ColumnEntity columnEntity = ColumnEntity.of("Person");
            columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

            Mockito.when(managerMock
                    .select(any(ColumnQuery.class)))
                    .thenReturn(Arrays.asList(columnEntity, columnEntity));

            ColumnQuery query = select().from("person").build();

            subject.singleResult(query);
        });
    }


    @Test
    public void shouldReturnErrorWhenFindIdHasIdNull() {
        Assertions.assertThrows(NullPointerException.class, () -> subject.find(Person.class, null));
    }

    @Test
    public void shouldReturnErrorWhenFindIdHasClassNull() {
        Assertions.assertThrows(NullPointerException.class, () -> subject.find(null, "10"));
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotIdInFind() {
        Assertions.assertThrows(IdNotFoundException.class, () -> subject.find(Job.class, "10"));
    }

    @Test
    public void shouldReturnFind() {
        subject.find(Person.class, "10");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        ColumnCondition condition = query.getCondition().get();

        assertEquals("Person", query.getColumnFamily());
        assertEquals(ColumnCondition.eq(Column.of("_id", 10L)), condition);
    }

    @Test
    public void shouldDeleteEntity() {
        subject.delete(Person.class, "10");
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        verify(managerMock).delete(queryCaptor.capture());

        ColumnDeleteQuery query = queryCaptor.getValue();

        ColumnCondition condition = query.getCondition().get();

        assertEquals("Person", query.getColumnFamily());
        assertEquals(ColumnCondition.eq(Column.of("_id", 10L)), condition);
    }


    @Test
    public void shouldExecuteQuery() {
        List<Person> people = subject.query("select * from Person");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getColumnFamily());
    }

    @Test
    public void shouldConvertEntity() {
        List<Movie> movies = subject.query("select * from Movie");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("movie", query.getColumnFamily());
    }

    @Test
    public void shouldPreparedStatement() {
        PreparedStatement preparedStatement = subject.prepare("select * from Person where name = @name");
        preparedStatement.bind("name", "Ada");
        preparedStatement.getResultList();
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("Person", query.getColumnFamily());
    }

    @Test
    public void shouldCount() {
        subject.count("Person");
        verify(managerMock).count("Person");
    }

    @Test
    public void shouldCountFromEntityClass() {
        subject.count(Person.class);
        verify(managerMock).count("Person");
    }
}