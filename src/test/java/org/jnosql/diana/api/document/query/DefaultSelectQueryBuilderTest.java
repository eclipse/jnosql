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
package org.jnosql.diana.api.document.query;

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentQuery;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.jnosql.diana.api.document.DocumentCondition.gt;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.select;
import static org.junit.Assert.*;


public class DefaultSelectQueryBuilderTest {


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        select("column", "column", null);
    }

    @Test
    public void shouldSelect() {
        String columnFamily = "documentCollection";
        DocumentQuery query = select().from(columnFamily).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getDocumentCollection());
    }

    @Test
    public void shouldSelectColumns() {
        String columnFamily = "columnFamily";
        DocumentQuery query = select("column", "column2").from(columnFamily).build();
        assertThat(query.getDocuments(), containsInAnyOrder("column", "column2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getDocumentCollection());
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
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentQuery query = select().from(columnFamily).where(condition).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition conditionWhere = query.getCondition().get();
        assertEquals(condition, conditionWhere);
    }

    @Test
    public void shouldSelectWhereAnd() {
        String columnFamily = "columnFamily";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentQuery query = select().from(columnFamily).where(condition).and(gt(Document.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition expected = eq(Document.of("name", "Ada")).and(gt(Document.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }

    @Test
    public void shouldSelectWhereOr() {
        String columnFamily = "columnFamily";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentQuery query = select().from(columnFamily).where(condition).or(gt(Document.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition expected = eq(Document.of("name", "Ada")).or(gt(Document.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereAndConditionIsNull() {
        String columnFamily = "columnFamily";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        select().from(columnFamily).where(condition).and(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereOrConditionIsNull() {
        String columnFamily = "columnFamily";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        select().from(columnFamily).where(condition).or(null);
    }

    @Test
    public void shouldSelectOrder() {
        String columnFamily = "columnFamily";
        DocumentQuery query = select().from(columnFamily).orderBy(Sort.of("name", ASC)).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getDocumentCollection());
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
        DocumentQuery query = select().from(columnFamily).limit(10).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getDocumentCollection());
        assertEquals(10L, query.getMaxResults());
    }

    @Test
    public void shouldSelectStart() {
        String columnFamily = "columnFamily";
        DocumentQuery query = select().from(columnFamily).start(10).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(columnFamily, query.getDocumentCollection());
        assertEquals(10L, query.getFirstResult());
    }
}