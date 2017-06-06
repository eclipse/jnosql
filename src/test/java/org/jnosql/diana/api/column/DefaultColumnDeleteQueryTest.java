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
package org.jnosql.diana.api.column;

import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class DefaultColumnDeleteQueryTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenColumnIsNull() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery.of(null, condition);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenConditionIsNull() {
        DefaultColumnDeleteQuery.of("collection", null);
    }

    @Test
    public void shouldCreateInstance() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("select", condition);
        assertEquals("select", query.getColumnFamily());
        assertEquals(condition, query.getCondition().get());
        assertTrue(query.getColumns().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldGetErrorWhenAddColumnIsNull() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("select", condition);
        query.add(null);
    }

    @Test
    public void shouldAddColumn() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("select", condition);
        query.add("column");
        assertThat(query.getColumns(), contains("column"));
    }

    @Test
    public void shouldRemoveColumn() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("select", condition);
        query.add("column");
        query.remove("column");
        assertTrue(query.getColumns().isEmpty());
    }



    @Test
    public void shouldAddAllColumns() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("select", condition);
        query.addAll(singletonList("column"));
        assertThat(query.getColumns(), contains("column"));
    }

    @Test
    public void shouldRemoveAllColumns() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("select", condition);
        query.add("column");
        query.removeAll(singletonList("column"));
        assertTrue(query.getColumns().isEmpty());
    }

    @Test
    public void shouldAnd() {
        ColumnDeleteQuery query = ColumnDeleteQuery.of("select");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        ColumnCondition condition1 = ColumnCondition.eq(Column.of("age", 10));

        query.and(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.and(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }

    @Test
    public void shouldSetCondition() {
        ColumnDeleteQuery query = ColumnDeleteQuery.of("select");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        query.with(condition);
        assertEquals(condition, query.getCondition().get());
    }

    @Test
    public void shouldOr() {
        ColumnDeleteQuery query = ColumnDeleteQuery.of("select");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        ColumnCondition condition1 = ColumnCondition.eq(Column.of("age", 10));

        query.or(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.or(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }

}