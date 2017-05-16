/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */
package org.jnosql.diana.api.column;

import org.jnosql.diana.api.Sort;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class DefaultColumnQueryTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnsNPE() {
        ColumnQuery.of(null);
    }

    @Test
    public void shouldCreateInstance() {
        ColumnQuery query = ColumnQuery.of("select");
        assertEquals("select", query.getColumnFamily());
        assertEquals(0, query.getFirstResult());
        assertEquals(-1, query.getMaxResults());
        assertTrue(query.getColumns().isEmpty());
        assertTrue(query.getSorts().isEmpty());
    }

    @Test
    public void shouldAddSort() {
        ColumnQuery query = ColumnQuery.of("select");
        query.addSort(Sort.of("column", ASC));
        assertThat(query.getSorts(), contains(Sort.of("column", ASC)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddSort() {
        ColumnQuery query = ColumnQuery.of("select");
        query.addSort(null);
    }

    @Test
    public void shouldAddColumn() {
        ColumnQuery query = ColumnQuery.of("select");
        query.addColumn("column");
        assertThat(query.getColumns(), contains("column"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddColumn() {
        ColumnQuery query = ColumnQuery.of("select");
        query.addColumn(null);
    }

    @Test
    public void shouldSetFirstResult() {
        ColumnQuery query = ColumnQuery.of("select");
        assertEquals(query, query.withFirstResult(10));
        assertEquals(10L, query.getFirstResult());
    }

    @Test
    public void shouldSetMaxResult() {
        ColumnQuery query = ColumnQuery.of("select");
        assertEquals(query, query.withMaxResults(10L));
        assertEquals(10L, query.getMaxResults());
    }

    @Test
    public void shouldSetCondition() {
        ColumnQuery query = ColumnQuery.of("select");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        assertEquals(query, query.with(condition));
        assertEquals(condition, query.getCondition().get());
    }


    @Test
    public void shouldAnd() {
        ColumnQuery query = ColumnQuery.of("select");
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
        ColumnQuery query = ColumnQuery.of("select");
        ColumnCondition condition = ColumnCondition.eq(Column.of("name", "Ada"));
        ColumnCondition condition1 = ColumnCondition.eq(Column.of("age", 10));

        query.or(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.or(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }


    @Test
    public void shouldReturnDeleteQuery() {
        ColumnQuery query = ColumnQuery.of("select");
        query.addColumn("column");
        query.and(ColumnCondition.eq(Column.of("name", "Ada")));

        ColumnDeleteQuery columnDeleteQuery = query.toDeleteQuery();
        assertEquals(query.getColumnFamily(), columnDeleteQuery.getColumnFamily());
        assertThat(query.getColumns(), contains(columnDeleteQuery.getColumns().get(0)));

    }
}