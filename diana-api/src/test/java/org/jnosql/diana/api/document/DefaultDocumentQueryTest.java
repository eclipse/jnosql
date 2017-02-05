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
package org.jnosql.diana.api.document;

import org.jnosql.diana.api.Sort;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class DefaultDocumentQueryTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnsNPE() {
        DocumentQuery.of(null);
    }

    @Test
    public void shouldCreateInstance() {
        DocumentQuery query = DocumentQuery.of("query");
        assertEquals("query", query.getCollection());
        assertEquals(0, query.getStart());
        assertEquals(-1, query.getLimit());
        assertTrue(query.getDocuments().isEmpty());
        assertTrue(query.getSorts().isEmpty());
    }

    @Test
    public void shouldAddSort() {
        DocumentQuery query = DocumentQuery.of("query");
        query.addSort(Sort.of("column", ASC));
        assertThat(query.getSorts(), contains(Sort.of("column", ASC)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddSort() {
        DocumentQuery query = DocumentQuery.of("query");
        query.addSort(null);
    }

    @Test
    public void shouldAddColumn() {
        DocumentQuery query = DocumentQuery.of("query");
        query.addColumn("column");
        assertThat(query.getDocuments(), contains("column"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddColumn() {
        DocumentQuery query = DocumentQuery.of("query");
        query.addColumn(null);
    }

    @Test
    public void shoudSetStart() {
        DocumentQuery query = DocumentQuery.of("query");
        query.setStart(10);
        assertEquals(10L, query.getStart());
    }

    @Test
    public void shoudSetLimit() {
        DocumentQuery query = DocumentQuery.of("query");
        query.setLimit(10L);
        assertEquals(10L, query.getLimit());
    }

    @Test
    public void shouldAnd() {
        DocumentQuery query = DocumentQuery.of("query");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DocumentCondition condition1 = DocumentCondition.eq(Document.of("age", 10));

        query.and(condition);
        assertEquals(condition, query.getCondition());

        assertNotNull(query.and(condition1).getCondition());
        assertNotEquals(condition, query.getCondition());
        assertNotEquals(condition1, query.getCondition());
    }

    @Test
    public void shouldOr() {
        DocumentQuery query = DocumentQuery.of("query");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DocumentCondition condition1 = DocumentCondition.eq(Document.of("age", 10));

        query.or(condition);
        assertEquals(condition, query.getCondition());

        assertNotNull(query.or(condition1).getCondition());
        assertNotEquals(condition, query.getCondition());
        assertNotEquals(condition1, query.getCondition());
    }


    @Test
    public void shouldReturnDeleteQuery() {
        DocumentQuery query = DocumentQuery.of("query");
        query.addColumn("column");
        query.and(DocumentCondition.eq(Document.of("name", "Ada")));

        DocumentDeleteQuery columnDeleteQuery = query.toDeleteQuery();
        assertEquals(query.getCollection(), columnDeleteQuery.getCollection());
        assertThat(query.getDocuments(), contains(columnDeleteQuery.getDocuments().get(0)));

    }
}