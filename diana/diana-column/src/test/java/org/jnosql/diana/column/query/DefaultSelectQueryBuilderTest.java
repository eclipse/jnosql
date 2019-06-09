/*
 *
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
 *
 */
package org.jnosql.diana.column.query;

import org.hamcrest.Matchers;
import org.jnosql.diana.Condition;
import org.jnosql.diana.Sort;
import jakarta.nosql.TypeReference;
import org.jnosql.diana.column.Column;
import org.jnosql.diana.column.ColumnCondition;
import org.jnosql.diana.column.ColumnEntity;
import org.jnosql.diana.column.ColumnFamilyManager;
import org.jnosql.diana.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.column.ColumnQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.column.ColumnCondition.eq;
import static org.jnosql.diana.column.query.ColumnQueryBuilder.select;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DefaultSelectQueryBuilderTest {


    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        Assertions.assertThrows(NullPointerException.class, () -> select("column", "column", null));
    }

    @Test
    public void shouldSelect() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

    @Test
    public void shouldSelectColumns() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select("column", "column2").from(columnFamily).build();
        assertThat(query.getColumns(), containsInAnyOrder("column", "column2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> select().from(null));
    }


    @Test
    public void shouldSelectOrderAsc() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).orderBy("name").asc().build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertThat(query.getSorts(), Matchers.contains(Sort.of("name", Sort.SortType.ASC)));
    }

    @Test
    public void shouldSelectOrderDesc() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).orderBy("name").desc().build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertThat(query.getSorts(), contains(Sort.of("name", Sort.SortType.DESC)));
    }

    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            String columnFamily = "columnFamily";
            select().from(columnFamily).orderBy(null);
        });
    }

    @Test
    public void shouldSelectLimit() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).limit(10).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(10L, query.getLimit());
    }

    @Test
    public void shouldSelectSkip() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).skip(10).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(10L, query.getSkip());
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").eq(name).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(name, column.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").like(name).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.LIKE, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(name, column.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").gt(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.GREATER_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").gte(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").lt(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.LESSER_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnQuery query = select().from(columnFamily).where("name").lte(value).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", column.getName());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String columnFamily = "columnFamily";
        Number valueA = 10;
        Number valueB = 20;
        ColumnQuery query = select().from(columnFamily).where("name").between(valueA, valueB).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();

        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals("name", column.getName());
        assertThat(column.get(new TypeReference<List<Number>>() {
        }), Matchers.contains(10, 20));
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").not().eq(name).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        ColumnCondition negate = column.get(ColumnCondition.class);
        assertTrue(query.getColumns().isEmpty());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(Condition.NOT, condition.getCondition());
        assertEquals(Condition.EQUALS, negate.getCondition());
        assertEquals("name", negate.getColumn().getName());
        assertEquals(name, negate.getColumn().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").eq(name).and("age").gt(10).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<List<ColumnCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, containsInAnyOrder(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10))));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnQuery query = select().from(columnFamily).where("name").eq(name).or("age").gt(10).build();
        ColumnCondition condition = query.getCondition().get();

        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<List<ColumnCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions, containsInAnyOrder(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10))));
    }

    @Test
    public void shouldSelectNegate() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).where("city").not().eq("Assis")
                .and("name").not().eq("Lucas").build();

        ColumnCondition condition = query.getCondition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.getColumnFamily());
        Column column = condition.getColumn();
        List<ColumnCondition> conditions = column.get(new TypeReference<List<ColumnCondition>>() {
        });

        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, containsInAnyOrder(eq(Column.of("city", "Assis")).negate(),
                eq(Column.of("name", "Lucas")).negate()));


    }

    @Test
    public void shouldExecuteManager() {
        ColumnFamilyManager manager = Mockito.mock(ColumnFamilyManager.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String columnFamily = "columnFamily";
        List<ColumnEntity> entities = select().from(columnFamily).execute(manager);
        Mockito.verify(manager).select(queryCaptor.capture());
        checkQuery(queryCaptor, columnFamily);
    }

    @Test
    public void shouldExecuteSingleResultManager() {
        ColumnFamilyManager manager = Mockito.mock(ColumnFamilyManager.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String columnFamily = "columnFamily";
        Optional<ColumnEntity> entities = select().from(columnFamily).executeSingle(manager);
        Mockito.verify(manager).singleResult(queryCaptor.capture());
        checkQuery(queryCaptor, columnFamily);
    }

    @Test
    public void shouldExecuteManagerAsync() {
        ColumnFamilyManagerAsync manager = Mockito.mock(ColumnFamilyManagerAsync.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String columnFamily = "columnFamily";
        Consumer<List<ColumnEntity>> callback = System.out::println;
        select().from(columnFamily).execute(manager, callback);
        Mockito.verify(manager).select(queryCaptor.capture(), Mockito.eq(callback));
        checkQuery(queryCaptor, columnFamily);
    }

    @Test
    public void shouldExecuteSingleResultManagerAsync() {
        ColumnFamilyManagerAsync manager = Mockito.mock(ColumnFamilyManagerAsync.class);
        ArgumentCaptor<ColumnQuery> queryCaptor = ArgumentCaptor.forClass(ColumnQuery.class);
        String columnFamily = "columnFamily";
        Consumer<Optional<ColumnEntity>> callback = System.out::println;
        select().from(columnFamily).executeSingle(manager, callback);
        Mockito.verify(manager).singleResult(queryCaptor.capture(), Mockito.eq(callback));
        checkQuery(queryCaptor, columnFamily);
    }


    private void checkQuery(ArgumentCaptor<ColumnQuery> queryCaptor, String columnFamily) {
        ColumnQuery query = queryCaptor.getValue();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

}