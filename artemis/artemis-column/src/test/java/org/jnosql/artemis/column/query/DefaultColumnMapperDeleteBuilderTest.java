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
package org.jnosql.artemis.column.query;

import org.jnosql.artemis.CDIExtension;
import org.jnosql.artemis.column.ColumnTemplate;
import org.jnosql.artemis.column.ColumnTemplateAsync;
import org.jnosql.artemis.model.Address;
import org.jnosql.artemis.model.Money;
import org.jnosql.artemis.model.Person;
import org.jnosql.artemis.model.Worker;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.jnosql.diana.api.column.query.ColumnQueryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.function.Consumer;

import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(CDIExtension.class)
public class DefaultColumnMapperDeleteBuilderTest {

    @Inject
    private ColumnQueryMapperBuilder mapperBuilder;


    @Test
    public void shouldReturnDeleteFrom() {
        ColumnMapperDeleteFrom columnFrom = mapperBuilder.deleteFrom(Person.class);
        ColumnDeleteQuery query = columnFrom.build();
        ColumnDeleteQuery queryExpected = ColumnQueryBuilder.delete().from("Person").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameEq() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").eq("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name").eq("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLike() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").like("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name").like("Ada").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGt() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").gt(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").gt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameGte() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").gte(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").gte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLt() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").lt(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").lt(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameLte() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").lte(10).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").lte(10L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id")
                .between(10, 20).build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L).build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("name").not().like("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("name").not().like("Ada").build();
        assertEquals(queryExpected, query);
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("age").between(10, 20)
                .and("name").eq("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("age")
                .between(10, 20)
                .and("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldSelectWhereNameOr() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").between(10, 20)
                .or("name").eq("Ada").build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id")
                .between(10L, 20L)
                .or("name").eq("Ada").build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldConvertField() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Person.class).where("id").eq("20")
                .build();
        ColumnDeleteQuery queryExpected = delete().from("Person").where("_id").eq(20L)
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldUseAttibuteConverter() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Worker.class).where("salary")
                .eq(new Money("USD", BigDecimal.TEN)).build();
        ColumnDeleteQuery queryExpected = delete().from("Worker").where("money")
                .eq("USD 10").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryByEmbeddable() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Worker.class).where("job.city").eq("Salvador")
                .build();
        ColumnDeleteQuery queryExpected = delete().from("Worker").where("city").eq("Salvador")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldQueryBySubEntity() {
        ColumnDeleteQuery query = mapperBuilder.deleteFrom(Address.class).where("zipcode.zip").eq("01312321")
                .build();
        ColumnDeleteQuery queryExpected = delete().from("Address").where("zipcode.zip").eq("01312321")
                .build();

        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldExecuteDeleteFrom() {
        ColumnTemplate template = Mockito.mock(ColumnTemplate.class);
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        mapperBuilder.deleteFrom(Person.class).execute(template);
        Mockito.verify(template).delete(queryCaptor.capture());
        ColumnDeleteQuery query = queryCaptor.getValue();
        ColumnDeleteQuery queryExpected = ColumnQueryBuilder.delete().from("Person").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldExecuteDeleteAsyncFrom() {
        ColumnTemplateAsync template = Mockito.mock(ColumnTemplateAsync.class);
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        mapperBuilder.deleteFrom(Person.class).execute(template);
        Mockito.verify(template).delete(queryCaptor.capture());
        ColumnDeleteQuery query = queryCaptor.getValue();
        ColumnDeleteQuery queryExpected = ColumnQueryBuilder.delete().from("Person").build();
        assertEquals(queryExpected, query);
    }

    @Test
    public void shouldExecuteDeleteAsyncCallbackFrom() {
        ColumnTemplateAsync template = Mockito.mock(ColumnTemplateAsync.class);
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        Consumer<Void> consumer = System.out::println;
        mapperBuilder.deleteFrom(Person.class).execute(template, consumer);
        Mockito.verify(template).delete(queryCaptor.capture(), eq(consumer));
        ColumnDeleteQuery query = queryCaptor.getValue();
        ColumnDeleteQuery queryExpected = ColumnQueryBuilder.delete().from("Person").build();
        assertEquals(queryExpected, query);
    }


}