/*
 *
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
 *
 */
package org.eclipse.jnosql.communication.column;


import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.column.ColumnCondition.eq;
import static org.eclipse.jnosql.communication.column.ColumnDeleteQuery.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultDeleteQueryBuilderTest {

    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        Assertions.assertThrows(NullPointerException.class, () -> delete("column", "column", null));
    }

    @Test
    public void shouldDelete() {
        String columnFamily = "columnFamily";
        ColumnDeleteQuery query = delete().from(columnFamily).build();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(columnFamily, query.columnFamily());
    }

    @Test
    public void shouldDeleteColumns() {
        String columnFamily = "columnFamily";
        ColumnDeleteQuery query = delete("column", "column2").from(columnFamily).build();
        assertThat(query.columns()).contains("column", "column2");
        assertFalse(query.condition().isPresent());
        assertEquals(columnFamily, query.columnFamily());
    }


    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> delete().from(null));
    }


    @Test
    public void shouldSelectWhereNameEq() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").eq(name).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", column.name());
        assertEquals(name, column.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").like(name).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", column.name());
        assertEquals(name, column.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").gt(value).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").gte(value).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").lt(value).build();
        ColumnCondition columnCondition = query.condition().get();

        Column column = columnCondition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.LESSER_THAN, columnCondition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String columnFamily = "columnFamily";
        Number value = 10;
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").lte(value).build();
        ColumnCondition columnCondition = query.condition().get();

        Column column = columnCondition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.LESSER_EQUALS_THAN, columnCondition.condition());
        assertEquals("name", column.name());
        assertEquals(value, column.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String columnFamily = "columnFamily";
        Number valueA = 10;
        Number valueB = 20;
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").between(valueA, valueB).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();

        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals("name", column.name());
        assertThat(column.get(new TypeReference<List<Number>>() {
        })).contains(10, 20);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").not().eq(name).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();
        ColumnCondition negate = column.get(ColumnCondition.class);
        assertTrue(query.columns().isEmpty());
        assertEquals(columnFamily, query.columnFamily());
        assertEquals(Condition.NOT, condition.condition());
        assertEquals(Condition.EQUALS, negate.condition());
        assertEquals("name", negate.column().name());
        assertEquals(name, negate.column().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").eq(name).and("age").gt(10).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10)));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String columnFamily = "columnFamily";
        String name = "Ada Lovelace";
        ColumnDeleteQuery query = delete().from(columnFamily).where("name").eq(name).or("age").gt(10).build();
        ColumnCondition condition = query.condition().get();

        Column column = condition.column();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Column.of("name", name)),
                ColumnCondition.gt(Column.of("age", 10)));
    }

    @Test
    public void shouldDeleteNegate() {
        String columnFamily = "columnFamily";
        ColumnDeleteQuery query = delete().from(columnFamily).where("city").not().eq("Assis")
                .and("name").not().eq("Lucas").build();

        ColumnCondition condition = query.condition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.columnFamily());
        Column column = condition.column();
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });

        assertEquals(Condition.AND, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Column.of("city", "Assis")).negate(),
                eq(Column.of("name", "Lucas")).negate());


    }

    @Test
    public void shouldExecuteDelete() {
        String columnFamily = "columnFamily";
        ColumnManager manager = Mockito.mock(ColumnManager.class);
        ArgumentCaptor<ColumnDeleteQuery> queryCaptor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        delete().from(columnFamily).delete(manager);
        verify(manager).delete(queryCaptor.capture());

        ColumnDeleteQuery query = queryCaptor.getValue();
        assertTrue(query.columns().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(columnFamily, query.columnFamily());
    }

}
