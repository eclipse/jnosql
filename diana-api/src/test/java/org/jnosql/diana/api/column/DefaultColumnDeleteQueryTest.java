/*
 * Copyright 2017 Eclipse Foundation
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("query", condition);
        assertEquals("query", query.getColumnFamily());
        assertEquals(condition, query.getCondition().get());
        assertTrue(query.getColumns().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldGetErrorWhenAddColumnIsNull() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("query", condition);
        query.add(null);
    }

    @Test
    public void shouldAddColumn() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("query", condition);
        query.add("column");
        assertThat(query.getColumns(), contains("column"));
    }

    @Test
    public void shouldRemoveColumn() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("query", condition);
        query.add("column");
        query.remove("column");
        assertTrue(query.getColumns().isEmpty());
    }



    @Test
    public void shouldAddAllColumns() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("query", condition);
        query.addAll(singletonList("column"));
        assertThat(query.getColumns(), contains("column"));
    }

    @Test
    public void shouldRemoveAllColumns() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteQuery query = DefaultColumnDeleteQuery.of("query", condition);
        query.add("column");
        query.removeAll(singletonList("column"));
        assertTrue(query.getColumns().isEmpty());
    }

    @Test
    public void shouldAnd() {
        ColumnDeleteQuery query = ColumnDeleteQuery.of("query");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        ColumnCondition condition1 = ColumnCondition.eq(Column.of("age", 10));

        query.and(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.and(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }

    @Test
    public void shouldOr() {
        ColumnDeleteQuery query = ColumnDeleteQuery.of("query");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        ColumnCondition condition1 = ColumnCondition.eq(Column.of("age", 10));

        query.or(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.or(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }

}