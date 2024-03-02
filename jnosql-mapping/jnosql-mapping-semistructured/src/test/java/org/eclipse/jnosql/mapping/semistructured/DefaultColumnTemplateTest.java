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

import jakarta.data.exceptions.NonUniqueResultException;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.nosql.PreparedStatement;
import org.assertj.core.api.SoftAssertions;
import org.eclipse.jnosql.communication.column.Column;
import org.eclipse.jnosql.communication.column.ColumnCondition;
import org.eclipse.jnosql.communication.column.ColumnDeleteQuery;
import org.eclipse.jnosql.communication.column.ColumnEntity;
import org.eclipse.jnosql.communication.column.ColumnManager;
import org.eclipse.jnosql.communication.column.ColumnQuery;
import org.eclipse.jnosql.mapping.core.Converters;
import org.eclipse.jnosql.mapping.IdNotFoundException;
import org.eclipse.jnosql.mapping.semistructured.entities.Job;
import org.eclipse.jnosql.mapping.semistructured.entities.Person;
import org.eclipse.jnosql.mapping.semistructured.spi.ColumnExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.core.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.eclipse.jnosql.communication.column.ColumnDeleteQuery.delete;
import static org.eclipse.jnosql.communication.column.ColumnQuery.select;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@EnableAutoWeld
@AddPackages(value = {Converters.class, ColumnEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, ColumnExtension.class})
class DefaultColumnTemplateTest {

    private final Person person = Person.builder().
            withAge().
            withPhones(Arrays.asList("234", "432")).
            withName("Name")
            .withId(19)
            .withIgnore().build();

    private final Column[] columns = new Column[]{
            Column.of("age", 10),
            Column.of("phones", Arrays.asList("234", "432")),
            Column.of("name", "Name"),
            Column.of("id", 19L),
    };

    @Inject
    private ColumnEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private ColumnManager managerMock;

    private DefaultColumnTemplate template;

    private ArgumentCaptor<ColumnEntity> captor;

    private ColumnEventPersistManager columnEventPersistManager;

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        managerMock = Mockito.mock(ColumnManager.class);
        columnEventPersistManager = Mockito.mock(ColumnEventPersistManager.class);
        captor = ArgumentCaptor.forClass(ColumnEntity.class);
        Instance<ColumnManager> instance = Mockito.mock(Instance.class);
        Mockito.when(instance.get()).thenReturn(managerMock);
        this.template = new DefaultColumnTemplate(converter, instance,
                columnEventPersistManager, entities, converters);
    }

    @Test
    void shouldInsert() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        template.insert(this.person);
        verify(managerMock).insert(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        ColumnEntity value = captor.getValue();
        assertEquals("Person", value.name());
        assertEquals(4, value.columns().size());
    }


    @Test
    void shouldMergeOnInsert() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        Person person = Person.builder().build();
        Person result = template.insert(person);
        verify(managerMock).insert(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        assertSame(person, result);
       assertEquals(10, person.getAge());

    }




    @Test
    void shouldInsertTTL() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class),
                        any(Duration.class)))
                .thenReturn(columnEntity);

        template.insert(this.person, Duration.ofHours(2));
        verify(managerMock).insert(captor.capture(), Mockito.eq(Duration.ofHours(2)));
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        ColumnEntity value = captor.getValue();
        assertEquals("Person", value.name());
        assertEquals(4, value.columns().size());
    }

    @Test
    void shouldUpdate() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        template.update(this.person);
        verify(managerMock).update(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        ColumnEntity value = captor.getValue();
        assertEquals("Person", value.name());
        assertEquals(4, value.columns().size());
    }

    @Test
    void shouldMergeOnUpdate() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        Person person = Person.builder().build();
        Person result = template.update(person);
        verify(managerMock).update(captor.capture());
        verify(columnEventPersistManager).firePostEntity(any(Person.class));
        verify(columnEventPersistManager).firePreEntity(any(Person.class));
        assertSame(person, result);
        assertEquals(10, person.getAge());

    }

    @Test
    void shouldInsertEntitiesTTL() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));
        Duration duration = Duration.ofHours(2);

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class), Mockito.eq(duration)))
                .thenReturn(columnEntity);

        template.insert(Arrays.asList(person, person), duration);
        verify(managerMock, times(2)).insert(any(ColumnEntity.class), any(Duration.class));
    }

    @Test
    void shouldInsertEntities() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .insert(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        template.insert(Arrays.asList(person, person));
        verify(managerMock, times(2)).insert(any(ColumnEntity.class));
    }

    @Test
    void shouldUpdateEntities() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .update(any(ColumnEntity.class)))
                .thenReturn(columnEntity);

        template.update(Arrays.asList(person, person));
        verify(managerMock, times(2)).update(any(ColumnEntity.class));
    }

    @Test
    void shouldDelete() {

        ColumnDeleteQuery query = delete().from("delete").build();
        template.delete(query);
        verify(managerMock).delete(query);
    }

    @Test
    void shouldSelect() {
        ColumnQuery query = select().from("person").build();
        template.select(query);
        verify(managerMock).select(query);
    }

    @Test
    void shouldCountBy() {
        ColumnQuery query = select().from("person").build();
        template.count(query);
        verify(managerMock).count(query);
    }

    @Test
    void shouldExist() {
        ColumnQuery query = select().from("person").build();
        template.exists(query);
        verify(managerMock).exists(query);
    }

    @Test
    void shouldReturnSingleResult() {
        ColumnEntity columnEntity = ColumnEntity.of("Person");
        columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

        Mockito.when(managerMock
                .select(any(ColumnQuery.class)))
                .thenReturn(Stream.of(columnEntity));

        ColumnQuery query = select().from("person").build();

        Optional<Person> result = template.singleResult(query);
        assertTrue(result.isPresent());
    }

    @Test
    void shouldReturnSingleResultIsEmpty() {
        Mockito.when(managerMock
                .select(any(ColumnQuery.class)))
                .thenReturn(Stream.empty());

        ColumnQuery query = select().from("person").build();

        Optional<Person> result = template.singleResult(query);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldReturnErrorWhenThereMoreThanASingleResult() {
        Assertions.assertThrows(NonUniqueResultException.class, () -> {
            ColumnEntity columnEntity = ColumnEntity.of("Person");
            columnEntity.addAll(Stream.of(columns).collect(Collectors.toList()));

            Mockito.when(managerMock
                    .select(any(ColumnQuery.class)))
                    .thenReturn(Stream.of(columnEntity, columnEntity));

            ColumnQuery query = select().from("person").build();

            template.singleResult(query);
        });
    }


    @Test
    void shouldReturnErrorWhenFindIdHasIdNull() {
        Assertions.assertThrows(NullPointerException.class, () -> template.find(Person.class, null));
    }

    @Test
    void shouldReturnErrorWhenFindIdHasClassNull() {
        Assertions.assertThrows(NullPointerException.class, () -> template.find(null, "10"));
    }

    @Test
    void shouldReturnErrorWhenThereIsNotIdInFind() {
        Assertions.assertThrows(IdNotFoundException.class, () -> template.find(Job.class, "10"));
    }

    @Test
    void shouldReturnFind() {
        template.find(Person.class, "10");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        ColumnCondition condition = query.condition().get();

        assertEquals("Person", query.name());
        assertEquals(ColumnCondition.eq(Column.of("_id", 10L)), condition);
    }

    @Test
    void shouldDeleteEntity() {
        template.delete(Person.class, "10");
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        verify(managerMock).delete(queryCaptor.capture());

        ColumnDeleteQuery query = queryCaptor.getValue();

        ColumnCondition condition = query.condition().get();

        assertEquals("Person", query.name());
        assertEquals(ColumnCondition.eq(Column.of("_id", 10L)), condition);
    }

    @Test
    void shouldExecuteQuery() {
        template.query("select * from Person");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("Person", query.name());
    }

    @Test
    void shouldConvertEntity() {
        template.query("select * from Movie");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("movie", query.name());
    }

    @Test
    void shouldConvertEntityName() {
        template.query("select * from download");
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("download", query.name());
    }
    @Test
    void shouldConvertEntityNameClassName() {
        template.query("select * from " + Person.class.getName());
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("Person", query.name());
    }

    @Test
    void shouldConvertConvertFromAnnotationEntity(){
        template.query("select * from Vendor" );
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("vendors", query.name());
    }

    @Test
    void shouldPreparedStatement() {
        PreparedStatement preparedStatement = template.prepare("select * from Person where name = @name");
        preparedStatement.bind("name", "Ada");
        preparedStatement.result();
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).select(queryCaptor.capture());
        ColumnQuery query = queryCaptor.getValue();
        assertEquals("Person", query.name());
    }

    @Test
    void shouldCount() {
        template.count("Person");
        verify(managerMock).count("Person");
    }

    @Test
    void shouldCountFromEntityClass() {
        template.count(Person.class);
        var captor = ArgumentCaptor.forClass(ColumnQuery.class);
        verify(managerMock).count(captor.capture());
        var query = captor.getValue();
        SoftAssertions.assertSoftly(soft ->{
            soft.assertThat(query.condition()).isEmpty();
        });
    }


    @Test
    void shouldFindAll(){
        template.findAll(Person.class);
        verify(managerMock).select(select().from("Person").build());
    }

    @Test
    void shouldDeleteAll(){
        template.deleteAll(Person.class);
        verify(managerMock).delete(delete().from("Person").build());
    }
}
