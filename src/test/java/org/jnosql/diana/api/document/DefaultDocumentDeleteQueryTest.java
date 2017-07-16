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
package org.jnosql.diana.api.document;

import org.junit.Test;

import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


public class DefaultDocumentDeleteQueryTest {

    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenColumnIsNull() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery.of(null, condition);
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnNPEWhenConditionIsNull() {
        DefaultDocumentDeleteQuery.of("collection", null);
    }

    @Test
    public void shouldCreateInstance() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery query = DefaultDocumentDeleteQuery.of("select", condition);
        assertEquals("select", query.getDocumentCollection());
        assertEquals(condition, query.getCondition().get());
        assertTrue(query.getDocuments().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void shouldGetErrorWhenAddColumnIsNull() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery query = DefaultDocumentDeleteQuery.of("select", condition);
        query.add(null);
    }

    @Test
    public void shouldAddColumn() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery query = DefaultDocumentDeleteQuery.of("select", condition);
        query.add("column");
        assertThat(query.getDocuments(), contains("column"));
    }

    @Test
    public void shouldRemoveColumn() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery query = DefaultDocumentDeleteQuery.of("select", condition);
        query.add("column");
        query.remove("column");
        assertTrue(query.getDocuments().isEmpty());
    }


    @Test
    public void shouldAddAllColumns() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery query = DefaultDocumentDeleteQuery.of("select", condition);
        query.addAll(singletonList("column"));
        assertThat(query.getDocuments(), contains("column"));
    }

    @Test
    public void shouldRemoveAllColumns() {
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DefaultDocumentDeleteQuery query = DefaultDocumentDeleteQuery.of("select", condition);
        query.add("column");
        query.removeAll(singletonList("column"));
        assertTrue(query.getDocuments().isEmpty());
    }

    @Test
    public void shouldAnd() {
        DocumentDeleteQuery query = DocumentDeleteQuery.of("select");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DocumentCondition condition1 = DocumentCondition.eq(Document.of("age", 10));

        query.and(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.and(condition1).getCondition());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }

    @Test
    public void shouldSetCondition() {
        DocumentDeleteQuery query = DocumentDeleteQuery.of("select");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));

        query.with(condition);
        assertEquals(condition, query.getCondition().get());
    }

    @Test
    public void shouldOr() {
        DocumentDeleteQuery query = DocumentDeleteQuery.of("select");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DocumentCondition condition1 = DocumentCondition.eq(Document.of("age", 10));

        query.or(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.or(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }
}