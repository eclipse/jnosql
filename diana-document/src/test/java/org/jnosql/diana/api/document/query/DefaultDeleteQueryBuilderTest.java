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


import org.hamcrest.Matchers;
import org.jnosql.diana.api.Condition;
import org.jnosql.diana.api.TypeReference;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

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

    //
    @Test
    public void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.EQUALS, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(name, document.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").like(name).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.LIKE, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(name, document.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").gt(value).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.GREATER_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").gte(value).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").lt(value).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.LESSER_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").lte(value).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.getCondition());
        assertEquals("name", document.getName());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String documentCollection = "documentCollection";
        Number valueA = 10;
        Number valueB = 20;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").between(valueA, valueB).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals("name", document.getName());
        Assert.assertThat(document.get(new TypeReference<List<Number>>() {}), Matchers.contains(10, 20));
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").not().eq(name).build();
        DocumentCondition condition = query.getCondition().get();

        Document column = condition.getDocument();
        DocumentCondition negate = column.get(DocumentCondition.class);
        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.NOT, condition.getCondition());
        assertEquals(Condition.EQUALS, negate.getCondition());
        assertEquals("name", negate.getDocument().getName());
        assertEquals(name, negate.getDocument().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name).and("age")
                .gt(10).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(DocumentCondition.eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name)
                .or("age").gt(10).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(DocumentCondition.eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }


    @Test
    public void shouldSelectWhereNameAnd2() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name)
                .and(DocumentCondition.gt(Document.of("age", 10))).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(DocumentCondition.eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }

    @Test
    public void shouldSelectWhereNameOr2() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name)
                .or(DocumentCondition.gt(Document.of("age", 10))).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(DocumentCondition.eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }


    @Test
    public void shouldSelectWhereNameAnd3() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentDeleteQuery query = delete().from(documentCollection)
                .where(DocumentCondition.eq(Document.of("name", name))
        ).and("age").gt(10).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(DocumentCondition.eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }

    @Test
    public void shouldSelectWhereNameOr3() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection)
                .where(DocumentCondition.eq(Document.of("name", name)))
                .or("age").gt(10).build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions, Matchers.containsInAnyOrder(DocumentCondition.eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10))));
    }
}
