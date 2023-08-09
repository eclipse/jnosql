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
import org.eclipse.jnosql.communication.document.DocumentDeleteQuery;
import org.eclipse.jnosql.communication.document.DocumentManager;
import org.eclipse.jnosql.mapping.Converters;
import org.eclipse.jnosql.mapping.document.entities.Address;
import org.eclipse.jnosql.mapping.document.entities.Money;
import org.eclipse.jnosql.mapping.document.entities.Person;
import org.eclipse.jnosql.mapping.document.entities.Worker;
import org.eclipse.jnosql.mapping.document.spi.DocumentExtension;
import org.eclipse.jnosql.mapping.metadata.EntitiesMetadata;
import org.eclipse.jnosql.mapping.reflection.Reflections;
import org.eclipse.jnosql.mapping.spi.EntityMetadataExtension;
import org.jboss.weld.junit5.auto.AddExtensions;
import org.jboss.weld.junit5.auto.AddPackages;
import org.jboss.weld.junit5.auto.EnableAutoWeld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.eclipse.jnosql.communication.document.DocumentDeleteQuery.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@EnableAutoWeld
@AddPackages(value = {Converters.class, DocumentEntityConverter.class})
@AddPackages(MockProducer.class)
@AddPackages(Reflections.class)
@AddExtensions({EntityMetadataExtension.class, DocumentExtension.class})
public class MapperDeleteTest {

    @Inject
    private DocumentEntityConverter converter;

    @Inject
    private EntitiesMetadata entities;

    @Inject
    private Converters converters;

    private DocumentManager managerMock;

    private DefaultDocumentTemplate template;


    private ArgumentCaptor<DocumentDeleteQuery> captor;

    @BeforeEach
    public void setUp() {
        managerMock = Mockito.mock(DocumentManager.class);
        DocumentEventPersistManager persistManager = Mockito.mock(DocumentEventPersistManager.class);
        Instance<DocumentManager> instance = Mockito.mock(Instance.class);
        this.captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        when(instance.get()).thenReturn(managerMock);
        this.template = new DefaultDocumentTemplate(converter, instance,
                persistManager, entities, converters);
    }

    @Test
    public void shouldReturnDeleteFrom() {
        template.delete(Person.class).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldDeleteWhereEq() {
        template.delete(Person.class).where("name").eq("Ada").execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("name").eq("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereLike() {
        template.delete(Person.class).where("name").like("Ada").execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("name").like("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereGt() {
        template.delete(Person.class).where("id").gt(10).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").gt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereGte() {
        template.delete(Person.class).where("id").gte(10).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").gte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereLt() {
        template.delete(Person.class).where("id").lt(10).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").lt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereLte() {
        template.delete(Person.class).where("id").lte(10).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").lte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereBetween() {
        template.delete(Person.class).where("id")
                .between(10, 20).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereNot() {
        template.delete(Person.class).where("name").not().like("Ada").execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("name").not().like("Ada").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldDeleteWhereAnd() {
        template.delete(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldDeleteWhereOr() {
        template.delete(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        template.delete(Person.class).where("id").eq("20")
                .execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").eq(20L)
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttributeConverter() {
        template.delete(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).execute();
        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Worker").where("money")
                .eq("USD 10").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        template.delete(Worker.class).where("job.city").eq("Salvador")
                .execute();

        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Worker").where("city").eq("Salvador")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        template.delete(Address.class).where("zipCode.zip").eq("01312321")
                .execute();

        Mockito.verify(managerMock).delete(captor.capture());
        DocumentDeleteQuery query = captor.getValue();

        DocumentDeleteQuery queryExpected = delete().from("Address").where("zipCode.zip").eq("01312321")
                .build();

        assertEquals(queryExpected, query);
    }

}
