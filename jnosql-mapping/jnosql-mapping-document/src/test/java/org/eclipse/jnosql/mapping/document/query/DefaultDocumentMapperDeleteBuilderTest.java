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
package org.eclipse.jnosql.mapping.document.query;

import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.mapping.document.DocumentQueryMapper;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperDeleteFrom;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;

import static jakarta.nosql.document.DocumentDeleteQuery.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class DefaultDocumentMapperDeleteBuilderTest {

    @Inject
    private DocumentQueryMapper mapperBuilder;


    @Test
    public void shouldReturnDeleteFrom() {
        DocumentMapperDeleteFrom documentFrom = mapperBuilder.deleteFrom(Person.class);
        DocumentDeleteQuery query = documentFrom.build();
        DocumentDeleteQuery queryExpected = delete().from("Person").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameEq() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").eq("Ada").build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("name").eq("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLike() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").like("Ada").build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("name").like("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGt() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").gt(10).build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").gt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGte() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").gte(10).build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").gte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLt() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").lt(10).build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").lt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLte() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").lte(10).build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").lte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id")
                .between(10, 20).build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").not().like("Ada").build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("name").not().like("Ada").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameOr() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").eq("20")
                .build();
        DocumentDeleteQuery queryExpected = delete().from("Person").where("_id").eq(20L)
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttributeConverter() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).build();
        DocumentDeleteQuery queryExpected = delete().from("Worker").where("money")
                .eq("USD 10").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Worker.class).where("job.city").eq("Salvador")
                .build();
        DocumentDeleteQuery queryExpected = delete().from("Worker").where("city").eq("Salvador")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        DocumentDeleteQuery query = mapperBuilder.deleteFrom(Address.class).where("zipCode.zip").eq("01312321")
                .build();
        DocumentDeleteQuery queryExpected = delete().from("Address").where("zipCode.zip").eq("01312321")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldExecuteDelete() {
        DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);

        mapperBuilder.deleteFrom(Person.class).delete(template);
        Mockito.verify(template).delete(queryCaptor.capture());
        DocumentDeleteQuery query = queryCaptor.getValue();
        DocumentDeleteQuery queryExpected = delete().from("Person").build();
        assertEquals(queryExpected, query);
    }

}