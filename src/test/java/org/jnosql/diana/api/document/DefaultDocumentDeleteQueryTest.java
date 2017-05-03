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
        assertEquals("select", query.getCollection());
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