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

import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.mapping.Page;
import jakarta.nosql.mapping.Pagination;
import jakarta.nosql.mapping.document.DocumentQueryMapper;
import jakarta.nosql.mapping.document.DocumentQueryMapper.DocumentMapperFrom;
import jakarta.nosql.mapping.document.DocumentQueryPagination;
import jakarta.nosql.mapping.document.DocumentTemplate;
import jakarta.nosql.tck.entities.Address;
import jakarta.nosql.tck.entities.Money;
import jakarta.nosql.tck.entities.Person;
import jakarta.nosql.tck.entities.Worker;
import jakarta.nosql.tck.test.CDIExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static jakarta.nosql.document.DocumentQuery.select;
import static org.junit.jupiter.api.Assertions.assertEquals;

@CDIExtension
public class DefaultDocumentMapperSelectBuilderTest {

    @Inject
    private DocumentQueryMapper mapperBuilder;


    @Test
    public void shouldReturnSelectStarFrom() {
        DocumentMapperFrom documentFrom = mapperBuilder.selectFrom(Person.class);
        DocumentQuery query = documentFrom.build();
        DocumentQuery queryExpected = select().from("Person").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectOrderAsc() {
        DocumentQuery query = mapperBuilder.selectFrom(Worker.class).orderBy("salary").asc().build();
        DocumentQuery queryExpected = select().from("Worker").orderBy("money").asc().build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectOrderDesc() {
        DocumentQuery query = mapperBuilder.selectFrom(Worker.class).orderBy("salary").desc().build();
        DocumentQuery queryExpected = select().from("Worker").orderBy("money").desc().build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> mapperBuilder.selectFrom(Worker.class).orderBy(null));
    }

    @Test
    public void shouldSelectLimit() {
        DocumentQuery query = mapperBuilder.selectFrom(Worker.class).limit(10).build();
        DocumentQuery queryExpected = select().from("Worker").limit(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectStart() {
        DocumentQuery query = mapperBuilder.selectFrom(Worker.class).skip(10).build();
        DocumentQuery queryExpected = select().from("Worker").skip(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameEq() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("name").eq("Ada").build();
        DocumentQuery queryExpected = select().from("Person").where("name").eq("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLike() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("name").like("Ada").build();
        DocumentQuery queryExpected = select().from("Person").where("name").like("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGt() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id").gt(10).build();
        DocumentQuery queryExpected = select().from("Person").where("_id").gt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGte() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id").gte(10).build();
        DocumentQuery queryExpected = select().from("Person").where("_id").gte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLt() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id").lt(10).build();
        DocumentQuery queryExpected = select().from("Person").where("_id").lt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLte() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id").lte(10).build();
        DocumentQuery queryExpected = select().from("Person").where("_id").lte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id")
                .between(10, 20).build();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .between(10L, 20L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("name").not().like("Ada").build();
        DocumentQuery queryExpected = select().from("Person").where("name").not().like("Ada").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").build();
        DocumentQuery queryExpected = select().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameOr() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").build();
        DocumentQuery queryExpected = select().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        DocumentQuery query = mapperBuilder.selectFrom(Person.class).where("id").eq("20")
                .build();
        DocumentQuery queryExpected = select().from("Person").where("_id").eq(20L)
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttributeConverter() {
        DocumentQuery query = mapperBuilder.selectFrom(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).build();
        DocumentQuery queryExpected = select().from("Worker").where("money")
                .eq("USD 10").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        DocumentQuery query = mapperBuilder.selectFrom(Worker.class).where("job.city").eq("Salvador")
                .build();
        DocumentQuery queryExpected = select().from("Worker").where("city").eq("Salvador")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        DocumentQuery query = mapperBuilder.selectFrom(Address.class).where("zipCode.zip").eq("01312321")
                .build();
        DocumentQuery queryExpected = select().from("Address").where("zipCode.zip").eq("01312321")
                .build();

        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldExecuteQuery() {
        DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        Stream<Person> people = mapperBuilder.selectFrom(Person.class).getResult(template);
        Mockito.verify(template).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        DocumentQuery queryExpected = select().from("Person").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldExecuteSingleQuery() {
        DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        Optional<Person> person = mapperBuilder.selectFrom(Person.class).getSingleResult(template);
        Mockito.verify(template).singleResult(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        DocumentQuery queryExpected = select().from("Person").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldCreateQueryWithPagination() {
        Pagination pagination = Pagination.page(2).size(2);
        DocumentMapperFrom columnFrom = mapperBuilder.selectFrom(Person.class);
        DocumentQuery query = columnFrom.build(pagination);
        assertEquals(pagination.getLimit(), query.getLimit());
        assertEquals(pagination.getSkip(), query.getSkip());
    }

    @Test
    public void shouldExecuteQueryPagination() {
        Pagination pagination = Pagination.page(2).size(2);
        DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        mapperBuilder.selectFrom(Person.class).getResult(template, pagination);
        Mockito.verify(template).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals(pagination.getLimit(), query.getLimit());
        assertEquals(pagination.getSkip(), query.getSkip());
    }

    @Test
    public void shouldExecuteSingleQueryPagination() {
        Pagination pagination = Pagination.page(2).size(2);
        DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        mapperBuilder.selectFrom(Person.class).getSingleResult(template, pagination);
        Mockito.verify(template).singleResult(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals(pagination.getLimit(), query.getLimit());
        assertEquals(pagination.getSkip(), query.getSkip());
    }

    @Test
    public void shouldCreatePage() {
        Pagination pagination = Pagination.page(2).size(2);
        DocumentTemplate template = Mockito.mock(DocumentTemplate.class);
        ArgumentCaptor<DocumentQueryPagination> queryCaptor = ArgumentCaptor.forClass(DocumentQueryPagination.class);
        Page<Person> page = mapperBuilder.selectFrom(Person.class).page(template, pagination);
        Mockito.verify(template).select(queryCaptor.capture());
        DocumentQuery query = queryCaptor.getValue();
        assertEquals(pagination.getLimit(), query.getLimit());
        assertEquals(pagination.getSkip(), query.getSkip());
    }
}