/*
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

import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;


public class DefaultColumnDeleteConditionTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenColumnIsNull() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition.of(null, condition);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenConditionIsNull() {
        DefaultColumnDeleteCondition.of("collection", null);
    }

    @Test
    public void shouldCreateInstance() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition query = DefaultColumnDeleteCondition.of("query", condition);
        assertEquals("query", query.getColumnFamily());
        assertEquals(condition, query.getCondition());
        assertTrue(query.getColumns().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldGetErrorWhenAddColumnIsNull() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition query = DefaultColumnDeleteCondition.of("query", condition);
        query.add(null);
    }

    @Test
    public void shouldAddColumn() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition query = DefaultColumnDeleteCondition.of("query", condition);
        query.add("column");
        assertThat(query.getColumns(), contains("column"));
    }

    @Test
    public void shouldRemoveColumn() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition query = DefaultColumnDeleteCondition.of("query", condition);
        query.add("column");
        query.remove("column");
        assertTrue(query.getColumns().isEmpty());
    }



    @Test
    public void shouldAddAllColumns() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition query = DefaultColumnDeleteCondition.of("query", condition);
        query.addAll(singletonList("column"));
        assertThat(query.getColumns(), contains("column"));
    }

    @Test
    public void shouldRemoveAllColumns() {
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        DefaultColumnDeleteCondition query = DefaultColumnDeleteCondition.of("query", condition);
        query.add("column");
        query.removeAll(singletonList("column"));
        assertTrue(query.getColumns().isEmpty());
    }

}