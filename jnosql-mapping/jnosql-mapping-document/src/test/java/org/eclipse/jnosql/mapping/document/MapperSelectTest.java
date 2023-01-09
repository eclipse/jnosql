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
package org.eclipse.jnosql.mapping.document;

import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Converters;
import jakarta.nosql.mapping.document.DocumentEntityConverter;
import jakarta.nosql.mapping.document.DocumentEventPersistManager;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.eclipse.jnosql.mapping.reflection.EntitiesMetadata;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static jakarta.nosql.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@CDIExtension
public class MapperSelectTest {

    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private DocumentManager managerMock;

    private DefaultDocumentTemplate template;


    private ArgumentCaptor<DocumentQuery> captor;

    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(DocumentManager.class);
        DocumentEventPersistManager persistManager = Mockito.mock(DocumentEventPersistManager.class);
        Instance<DocumentManager> instance = Mockito.mock(Instance.class);
        this.captor = ArgumentCaptor.forClass(DocumentQuery.class);
        when(instance.get()).thenReturn(managerMock);
        DefaultDocumentWorkflow workflow = new DefaultDocumentWorkflow(persistManager, converter);
        this.template = new DefaultDocumentTemplate(converter, instance, workflow,
                persistManager, entities, converters);
    }


    @Test
    @DisplayName("Should execute 'select * from Person'")
    public void shouldExecuteSelectFrom() {
        template.select(Person.class).result();
        DocumentQuery queryExpected = select().from("Person").build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    @DisplayName("Should execute 'select * from Worker order by salary asc'")
    public void shouldSelectOrderAsc() {
        template.select(Worker.class).orderBy("salary").asc().result();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        DocumentQuery queryExpected = select().from("Worker").orderBy("money").asc().build();
        assertEquals(queryExpected, query);
    }

    @Test
    @DisplayName("Should execute 'select * from Worker order by salary desc'")
    public void shouldSelectOrderDesc() {
        template.select(Worker.class).orderBy("salary").desc().result();
        DocumentQuery queryExpected = select().from("Worker").orderBy("money").desc().build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    @DisplayName("Should execute 'select * from Worker limit 10'")
    public void shouldSelectLimit() {
        template.select(Worker.class).limit(10).result();
        DocumentQuery queryExpected = select().from("Worker").limit(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    @DisplayName("Should execute 'select * from Worker skip 10'")
    public void shouldSelectStart() {
        template.select(Worker.class).skip(10).result();
        DocumentQuery queryExpected = select().from("Worker").skip(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }



    @Test
    public void shouldSelectWhereNameEq() {
        template.select(Person.class).where("name").eq("Ada").result();
        DocumentQuery queryExpected = select().from("Person").where("name")
                .eq("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLike() {
        template.select(Person.class).where("name").like("Ada").result();
        DocumentQuery queryExpected = select().from("Person").where("name")
                .like("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGt() {
        template.select(Person.class).where("id").gt(10).result();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .gt(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGte() {
        template.select(Person.class).where("id").gte(10).result();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .gte(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }



    @Test
    public void shouldSelectWhereNameLt() {
        template.select(Person.class).where("id").lt(10).result();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .lt(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLte() {
        template.select(Person.class).where("id").lte(10).result();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .lte(10L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameBetween() {
       template.select(Person.class).where("id")
                .between(10, 20).result();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .between(10L, 20L).build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        template.select(Person.class).where("name").not().like("Ada").result();
        DocumentQuery queryExpected = select().from("Person").where("name")
                .not().like("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        template.select(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").result();
        DocumentQuery queryExpected = select().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();
        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameOr() {
        template.select(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").result();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        template.select(Person.class).where("id").eq("20")
                .result();
        DocumentQuery queryExpected = select().from("Person").where("_id").eq(20L)
                .build();

        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttributeConverter() {
        template.select(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).result();
        DocumentQuery queryExpected = select().from("Worker").where("money")
                .eq("USD 10").build();

        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        template.select(Worker.class).where("job.city").eq("Salvador")
                .result();
        DocumentQuery queryExpected = select().from("Worker").where("city")
                .eq("Salvador")
                .build();

        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        template.select(Address.class).where("zipCode.zip").eq("01312321")
                .result();
        DocumentQuery queryExpected = select().from("Address").where("zipCode.zip")
                .eq("01312321")
                .build();

        Mockito.verify(managerMock).select(captor.capture());
        DocumentQuery query = captor.getValue();
        assertEquals(queryExpected, query);
    }



    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> template.select(Worker.class).orderBy(null));
    }

}