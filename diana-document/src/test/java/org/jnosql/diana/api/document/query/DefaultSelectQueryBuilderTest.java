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
        select("document", "document'", null);
    }

    @Test
    public void shouldSelect() {
        String documentCollection = "documentCollection";
        DocumentQuery query = select().from(documentCollection).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }

    @Test
    public void shouldSelectDocument() {
        String documentCollection = "documentCollection";
        DocumentQuery query = select("document", "document2").from(documentCollection).build();
        assertThat(query.getDocuments(), containsInAnyOrder("document", "document2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenFromIsNull() {
        select().from(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenWhereConditionIsNull() {
        String documentCollection = "documentCollection";
        select().from(documentCollection).where((DocumentCondition) null);
    }

    @Test
    public void shouldSelectWhere() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentQuery query = select().from(documentCollection).where(condition).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition conditionWhere = query.getCondition().get();
        assertEquals(condition, conditionWhere);
    }

    @Test
    public void shouldSelectWhereAnd() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentQuery query = select().from(documentCollection).where(condition).and(gt(Document.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition expected = eq(Document.of("name", "Ada")).and(gt(Document.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }

    @Test
    public void shouldSelectWhereOr() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentQuery query = select().from(documentCollection).where(condition).or(gt(Document.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition expected = eq(Document.of("name", "Ada")).or(gt(Document.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereAndConditionIsNull() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        select().from(documentCollection).where(condition).and((DocumentCondition) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereOrConditionIsNull() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        select().from(documentCollection).where(condition).or((DocumentCondition) null);
    }

    @Test
    public void shouldSelectOrder() {
        String documentCollection = "documentCollection";
        DocumentQuery query = select().from(documentCollection).orderBy(Sort.of("name", ASC)).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertThat(query.getSorts(), contains(Sort.of("name", ASC)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        String documentCollection = "documentCollection";
        select().from(documentCollection).orderBy(null);
    }

    @Test
    public void shouldSelectLimit() {
        String documentCollection = "documentCollection";
        DocumentQuery query = select().from(documentCollection).limit(10).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(10L, query.getMaxResults());
    }

    @Test
    public void shouldSelectStart() {
        String documentCollection = "documentCollection";
        DocumentQuery query = select().from(documentCollection).start(10).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(10L, query.getFirstResult());
    }
}