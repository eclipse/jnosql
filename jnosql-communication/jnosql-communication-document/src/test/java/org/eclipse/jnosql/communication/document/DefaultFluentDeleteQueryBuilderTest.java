/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
package org.eclipse.jnosql.communication.document;


import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.document.DocumentCondition.eq;
import static org.eclipse.jnosql.communication.document.DocumentDeleteQuery.delete;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DefaultFluentDeleteQueryBuilderTest {

    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        Assertions.assertThrows(NullPointerException.class,() -> delete("document", "document", null));
    }

    @Test
    public void shouldDelete() {
        String documentCollection = "documentCollection";
        DocumentDeleteQuery query = delete().from(documentCollection).build();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.documentCollection());
    }

    @Test
    public void shouldDeleteDocuments() {
        String documentCollection = "documentCollection";
        DocumentDeleteQuery query = delete("document", "document2").from(documentCollection).build();
        assertThat(query.documents()).contains("document", "document2");
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.documentCollection());
    }


    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        Assertions.assertThrows(NullPointerException.class,() -> delete().from(null));
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", document.name());
        assertEquals(name, document.get());

    }

    @Test
    public void shouldSelectWhereNameLike() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").like(name).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", document.name());
        assertEquals(name, document.get());
    }

    @Test
    public void shouldSelectWhereNameGt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").gt(value).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameGte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").gte(value).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameLt() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").lt(value).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameLte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").lte(value).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    public void shouldSelectWhereNameBetween() {
        String documentCollection = "documentCollection";
        Number valueA = 10;
        Number valueB = 20;
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").between(valueA, valueB).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals("name", document.name());
        assertThat(document.get(new TypeReference<List<Number>>() {})).contains(10, 20);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").not().eq(name).build();
        DocumentCondition condition = query.condition().get();

        Document column = condition.document();
        DocumentCondition negate = column.get(DocumentCondition.class);
        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.documentCollection());
        assertEquals(Condition.NOT, condition.condition());
        assertEquals(Condition.EQUALS, negate.condition());
        assertEquals("name", negate.document().name());
        assertEquals(name, negate.document().get());
    }


    @Test
    public void shouldSelectWhereNameAnd() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name).and("age")
                .gt(10).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10)));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentDeleteQuery query = delete().from(documentCollection).where("name").eq(name)
                .or("age").gt(10).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10)));
    }



    @Test
    public void shouldDeleteNegate() {
        String columnFamily = "columnFamily";
        DocumentDeleteQuery query = delete().from(columnFamily).where("city").not().eq("Assis")
                .and("name").not().eq("Lucas").build();

        DocumentCondition condition = query.condition().orElseThrow(RuntimeException::new);
        assertEquals(columnFamily, query.documentCollection());
        Document column = condition.document();
        List<DocumentCondition> conditions = column.get(new TypeReference<>() {
        });

        assertEquals(Condition.AND, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Document.of("city", "Assis")).negate(),
                eq(Document.of("name", "Lucas")).negate());


    }

    @Test
    public void shouldExecuteDelete() {
        String collection = "collection";
        DocumentManager manager = Mockito.mock(DocumentManager.class);
        ArgumentCaptor<DocumentDeleteQuery> queryCaptor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        delete().from(collection).delete(manager);
        verify(manager).delete(queryCaptor.capture());

        DocumentDeleteQuery query = queryCaptor.getValue();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(collection, query.documentCollection());
    }
}
