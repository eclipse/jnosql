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

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnCondition;
import org.jnosql.diana.api.column.ColumnQuery;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.column.ColumnCondition.eq;
import static org.jnosql.diana.api.column.ColumnCondition.gt;
import static org.jnosql.diana.api.column.query.ColumnQueryBuilder.select;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultSelectQueryBuilderTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        select("column", "column", null);
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

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenFromIsNull() {
        select().from(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenWhereConditionIsNull() {
        String columnFamily = "columnFamily";
        select().from(columnFamily).where(null);
    }

    @Test
    public void shouldSelectWhere() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        ColumnQuery query = select().from(columnFamily).where(condition).build();
        assertTrue(query.getCondition().isPresent());
        ColumnCondition conditionWhere = query.getCondition().get();
        assertEquals(condition, conditionWhere);
    }

    @Test
    public void shouldSelectWhereAnd() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        ColumnQuery query = select().from(columnFamily).where(condition).and(gt(Column.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        ColumnCondition expected = eq(Column.of("name", "Ada")).and(gt(Column.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }

    @Test
    public void shouldSelectWhereOr() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        ColumnQuery query = select().from(columnFamily).where(condition).or(gt(Column.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        ColumnCondition expected = eq(Column.of("name", "Ada")).or(gt(Column.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereAndConditionIsNull() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        select().from(columnFamily).where(condition).and(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereOrConditionIsNull() {
        String columnFamily = "columnFamily";
        ColumnCondition condition = eq(Column.of("name", "Ada"));
        select().from(columnFamily).where(condition).or(null);
    }

    @Test
    public void shouldSelectOrder() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).orderBy(Sort.of("name", ASC)).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertThat(query.getSorts(), contains(Sort.of("name", ASC)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        String columnFamily = "columnFamily";
        select().from(columnFamily).orderBy(null);
    }

    @Test
    public void shouldSelectLimit() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).limit(10).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(10L, query.getMaxResults());
    }

    @Test
    public void shouldSelectStart() {
        String columnFamily = "columnFamily";
        ColumnQuery query = select().from(columnFamily).start(10).build();
        assertTrue(query.getColumns().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getColumnFamily());
        assertEquals(10L, query.getFirstResult());
    }

}