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

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnManager;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.column.ColumnEntityConverter;
import jakarta.nosql.mapping.column.ColumnEventPersistManager;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static jakarta.nosql.column.ColumnQuery.select;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@CDIExtension
public class MapperSelectTest {

    @Inject
    private ColumnEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private ColumnManager managerMock;

    private DefaultColumnTemplate template;

    private ArgumentCaptor<ColumnQuery> captor;

    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(ColumnManager.class);
        ColumnEventPersistManager persistManager = Mockito.mock(ColumnEventPersistManager.class);
        Instance<ColumnManager> instance = Mockito.mock(Instance.class);
        this.captor = ArgumentCaptor.forClass(ColumnQuery.class);
        when(instance.get()).thenReturn(managerMock);
        DefaultColumnWorkflow workflow = new DefaultColumnWorkflow(persistManager, converter);
        this.template = new DefaultColumnTemplate(converter, instance, workflow,
                persistManager, entities, converters);
    }


    @Test
    public void shouldExecuteSelectFrom() {
        template.select(Person.class).result();
        ColumnQuery queryExpected = select().from("Person").build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectOrderAsc() {
        template.select(Worker.class).orderBy("salary").asc().result();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        ColumnQuery queryExpected = select().from("Worker").orderBy("money").asc().build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectOrderDesc() {
        template.select(Worker.class).orderBy("salary").desc().result();
        ColumnQuery queryExpected = select().from("Worker").orderBy("money").desc().build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectLimit() {
        template.select(Worker.class).limit(10).result();
        ColumnQuery queryExpected = select().from("Worker").limit(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectStart() {
        template.select(Worker.class).skip(10).result();
        ColumnQuery queryExpected = select().from("Worker").skip(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameEq() {
        template.select(Person.class).where("name").eq("Ada").result();
        ColumnQuery queryExpected = select().from("Person").where("name")
                .eq("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLike() {
        template.select(Person.class).where("name").like("Ada").result();
        ColumnQuery queryExpected = select().from("Person").where("name")
                .like("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGt() {
        template.select(Person.class).where("id").gt(10).result();
        ColumnQuery queryExpected = select().from("Person").where("_id")
                .gt(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGte() {
        template.select(Person.class).where("id").gte(10).result();
        ColumnQuery queryExpected = select().from("Person").where("_id")
                .gte(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameLt() {
        template.select(Person.class).where("id").lt(10).result();
        ColumnQuery queryExpected = select().from("Person").where("_id")
                .lt(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLte() {
        template.select(Person.class).where("id").lte(10).result();
        ColumnQuery queryExpected = select().from("Person").where("_id")
                .lte(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        template.select(Person.class).where("id")
                .between(10, 20).result();
        ColumnQuery queryExpected = select().from("Person").where("_id")
                .between(10L, 20L).build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        template.select(Person.class).where("name").not().like("Ada").result();
        ColumnQuery queryExpected = select().from("Person").where("name")
                .not().like("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        template.select(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").result();
        ColumnQuery queryExpected = select().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameOr() {
        template.select(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").result();
        ColumnQuery queryExpected = select().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        template.select(Person.class).where("id").eq("20")
                .result();
        ColumnQuery queryExpected = select().from("Person").where("_id").eq(20L)
                .build();

        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttributeConverter() {
        template.select(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).result();
        ColumnQuery queryExpected = select().from("Worker").where("money")
                .eq("USD 10").build();

        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        template.select(Worker.class).where("job.city").eq("Salvador")
                .result();
        ColumnQuery queryExpected = select().from("Worker").where("city")
                .eq("Salvador")
                .build();

        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        template.select(Address.class).where("zipCode.zip").eq("01312321")
                .result();
        ColumnQuery queryExpected = select().from("Address").where("zipCode.zip")
                .eq("01312321")
                .build();

        Mockito.verify(managerMock).select(captor.capture());
        ColumnQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldResult() {
        ColumnQuery query = select().from("Person").build();
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add("_id", 1L);
        entity.add("name", "Ada");
        entity.add("age", 20);
        Mockito.when(managerMock.select(Mockito.eq(query))).thenReturn(Stream.of(entity));
        List<Person> result = template.select(Person.class).result();
        Assertions.assertNotNull(result);
        assertThat(result).hasSize(1)
                .map(Person::getName).contains("Ada");
    }


    @Test
    public void shouldStream() {

        ColumnQuery query = select().from("Person").build();
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add("_id", 1L);
        entity.add("name", "Ada");
        entity.add("age", 20);
        Mockito.when(managerMock.select(Mockito.eq(query))).thenReturn(Stream.of(entity));
        Stream<Person> result = template.select(Person.class).stream();
        Assertions.assertNotNull(result);
    }

    @Test
    public void shouldSingleResult() {

        ColumnQuery query = select().from("Person").build();
        ColumnEntity entity = ColumnEntity.of("Person");
        entity.add("_id", 1L);
        entity.add("name", "Ada");
        entity.add("age", 20);
        Mockito.when(managerMock.select(Mockito.eq(query))).thenReturn(Stream.of(entity));
        Optional<Person> result = template.select(Person.class).singleResult();
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.isPresent());
    }

    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> template.select(Worker.class).orderBy(null));
    }

}