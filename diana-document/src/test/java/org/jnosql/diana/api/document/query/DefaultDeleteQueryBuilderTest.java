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


import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.jnosql.diana.api.document.DocumentCondition.eq;
import static org.jnosql.diana.api.document.DocumentCondition.gt;
import static org.jnosql.diana.api.document.query.DocumentQueryBuilder.delete;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class DefaultDeleteQueryBuilderTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        delete("document", "document", null);
    }

    @Test
    public void shouldDelete() {
        String documentCollection = "documentCollection";
        DocumentDeleteQuery query = delete().from(documentCollection).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }

    @Test
    public void shouldDeleteDocuments() {
        String documentCollection = "documentCollection";
        DocumentDeleteQuery query = delete("document", "document2").from(documentCollection).build();
        assertThat(query.getDocuments(), containsInAnyOrder("document", "document2"));
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenFromIsNull() {
        delete().from(null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenWhereConditionIsNull() {
        String documentCollection = "documentCollection";
        delete().from(documentCollection).where((DocumentCondition) null);
    }

    @Test
    public void shouldSelectWhere() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentDeleteQuery query = delete().from(documentCollection).where(condition).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition conditionWhere = query.getCondition().get();
        assertEquals(condition, conditionWhere);
    }

    @Test
    public void shouldSelectWhereAnd() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentDeleteQuery query = delete().from(documentCollection).where(condition).and(gt(Document.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition expected = eq(Document.of("name", "Ada")).and(gt(Document.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }

    @Test
    public void shouldSelectWhereOr() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        DocumentDeleteQuery query = delete().from(documentCollection).where(condition).or(gt(Document.of("age", 10))).build();
        assertTrue(query.getCondition().isPresent());
        DocumentCondition expected = eq(Document.of("name", "Ada")).or(gt(Document.of("age", 10)));
        assertEquals(expected, query.getCondition().get());
    }


    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereAndConditionIsNull() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        delete().from(documentCollection).where(condition).and((DocumentCondition) null);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenSelectWhereOrConditionIsNull() {
        String documentCollection = "documentCollection";
        DocumentCondition condition = eq(Document.of("name", "Ada"));
        delete().from(documentCollection).where(condition).or((DocumentCondition) null);
    }
}
