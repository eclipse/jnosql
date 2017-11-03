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
package org.jnosql.diana.api.column.query;


import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnDeleteQuery;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.column.ColumnCondition.eq;
import static org.jnosql.diana.api.column.ColumnCondition.gt;
import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.delete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultDeleteQueryBuilderTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        delete("column", "column", null);
    }

    @Test
    public void shouldDelete() {
        String columnFamily = "columnFamily";
        ColumnDeleteQuery query = delete().from(columnFamily).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }

    @Test
    public void shouldDeleteColumns() {
        String columnFamily = "columnFamily";
        ColumnDeleteQuery query = delete("column", "column2").from(columnFamily).build();
        assertThat(query.getColumns(), containsInAnyOrder("column", "column2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenFromIsNull() {
        delete().from(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenWhereConditionIsNull() {
        String columnFamily = "columnFamily";
        delete().from(columnFamily).where((ColumnCondition) null);
    }

    @Test
    public void shouldSelectWhere() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        ColumnDeleteQuery query = delete().from(columnFamily).where(condition).build();
        assertTrue(query.getCondition().isPresent());
        ColumnCondition conditionWhere = query.getCondition().get();
        assertEquals(condition, conditionWhere);
    }

    @Test
    public void shouldSelectWhereAnd() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        ColumnDeleteQuery query = delete().from(columnFamily).where(condition).and(gt(Column.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        ColumnCondition expected = eq(Column.of("name", "Ada")).and(gt(Column.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }

    @Test
    public void shouldSelectWhereOr() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        ColumnDeleteQuery query = delete().from(columnFamily).where(condition).or(gt(Column.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        ColumnCondition expected = eq(Column.of("name", "Ada")).or(gt(Column.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereAndConditionIsNull() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        delete().from(columnFamily).where(condition).and(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereOrConditionIsNull() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        delete().from(columnFamily).where(condition).or(null);
    }
}
