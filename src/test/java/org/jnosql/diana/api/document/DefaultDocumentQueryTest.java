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
        DocumentQuery query = DocumentQuery.of("select");
        assertEquals("select", query.getCollection());
        assertEquals(0, query.getFirstResult());
        assertEquals(-1, query.getMaxResults());
        assertTrue(query.getDocuments().isEmpty());
        assertTrue(query.getSorts().isEmpty());
    }

    @Test
    public void shouldAddSort() {
        DocumentQuery query = DocumentQuery.of("select");
        query.addSort(Sort.of("column", ASC));
        assertThat(query.getSorts(), contains(Sort.of("column", ASC)));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddSort() {
        DocumentQuery query = DocumentQuery.of("select");
        query.addSort(null);
    }

    @Test
    public void shouldAddColumn() {
        DocumentQuery query = DocumentQuery.of("select");
        query.addColumn("column");
        assertThat(query.getDocuments(), contains("column"));
    }

    @Test(expected = NullPointerException.class)
    public void shouldReturnErrorWhenAddColumn() {
        DocumentQuery query = DocumentQuery.of("select");
        query.addColumn(null);
    }

    @Test
    public void shouldSetFirstResult() {
        DocumentQuery query = DocumentQuery.of("select");
        assertEquals(query, query.withFirstResult(10));
        assertEquals(10L, query.getFirstResult());
    }

    @Test
    public void shouldSetMaxResults() {
        DocumentQuery query = DocumentQuery.of("select");
        assertEquals(query, query.withMaxResults(10L));
        assertEquals(10L, query.getMaxResults());
    }

    @Test
    public void shouldSetCondition() {
        DocumentQuery query = DocumentQuery.of("select");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        assertEquals(query, query.with(condition));
        assertEquals(condition, query.getCondition().get());
    }

    @Test
    public void shouldAnd() {
        DocumentQuery query = DocumentQuery.of("select");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DocumentCondition condition1 = DocumentCondition.eq(Document.of("age", 10));

        query.and(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.and(condition1).getCondition());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }

    @Test
    public void shouldOr() {
        DocumentQuery query = DocumentQuery.of("select");
        DocumentCondition condition = DocumentCondition.eq(Document.of("name", "Ada"));
        DocumentCondition condition1 = DocumentCondition.eq(Document.of("age", 10));

        query.or(condition);
        assertEquals(condition, query.getCondition().get());

        assertNotNull(query.or(condition1).getCondition().get());
        assertNotEquals(condition, query.getCondition().get());
        assertNotEquals(condition1, query.getCondition().get());
    }


    @Test
    public void shouldReturnDeleteQuery() {
        DocumentQuery query = DocumentQuery.of("select");
        query.addColumn("column");
        query.and(DocumentCondition.eq(Document.of("name", "Ada")));

        DocumentDeleteQuery columnDeleteQuery = query.toDeleteQuery();
        assertEquals(query.getCollection(), columnDeleteQuery.getCollection());
        assertThat(query.getDocuments(), contains(columnDeleteQuery.getDocuments().get(0)));

    }
}