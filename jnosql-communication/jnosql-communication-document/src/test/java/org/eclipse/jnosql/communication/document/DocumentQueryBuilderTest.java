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
import jakarta.data.repository.Sort;
import jakarta.data.repository.Direction;
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.document.DocumentCondition.eq;
import static org.eclipse.jnosql.communication.document.DocumentQuery.builder;
import static org.junit.jupiter.api.Assertions.*;

class DocumentQueryBuilderTest {
    @Test
    void shouldReturnErrorWhenHasNullElementInSelect() {
        assertThrows(NullPointerException.class, () -> builder("document", "document'", null));
    }

    @Test
    void shouldBuilder() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).build();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.name());
    }

    @Test
    void shouldSelectDocument() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder("document", "document2").from(documentCollection).build();
        assertThat(query.documents()).contains("document", "document2");
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.name());
    }

    @Test
    void shouldReturnErrorWhenFromIsNull() {
        assertThrows(NullPointerException.class, () -> builder().from(null));
    }


    @Test
    void shouldSelectOrderAsc() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).sort(Sort.asc("name")).build();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.name());
        assertThat(query.sorts()).contains(Sort.of("name", Direction.ASC, false));
    }

    @Test
    void shouldSelectOrderDesc() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).sort(Sort.desc("name")).build();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.name());
        assertThat(query.sorts()).contains(Sort.of("name", Direction.DESC, false));
    }


    @Test
    void shouldReturnErrorSelectWhenOrderIsNull() {
        assertThrows(NullPointerException.class,() -> {
            String documentCollection = "documentCollection";
            builder().from(documentCollection).sort((Sort) null);
        });
    }

    @Test
    void shouldSelectLimit() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).limit(10).build();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.name());
        assertEquals(10L, query.limit());
    }

    @Test
    void shouldReturnErrorWhenLimitIsNegative() {
        String documentCollection = "documentCollection";
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder().from(documentCollection).limit(-1));
    }

    @Test
    void shouldSelectSkip() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).skip(10).build();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(documentCollection, query.name());
        assertEquals(10L, query.skip());
    }

    @Test
    void shouldReturnErrorWhenSkipIsNegative() {
        String documentCollection = "documentCollection";
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder().from(documentCollection).skip(-1));
    }

    @Test
    void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentQuery query = builder().from(documentCollection)
                .where(eq("name", name))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", document.name());
        assertEquals(name, document.get());

    }

    @Test
    void shouldSelectWhereNameLike() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.like("name", name))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", document.name());
        assertEquals(name, document.get());
    }

    @Test
    void shouldSelectWhereNameGt() {
        String documentCollection = "documentCollection";
        Number value = 10;

        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.gt("name", 10))
                .build();

        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    void shouldSelectWhereNameGte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentCondition gteName = DocumentCondition.gte("name", value);
        DocumentQuery query = builder().from(documentCollection).where(gteName).build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    void shouldSelectWhereNameLt() {
        String documentCollection = "documentCollection";
        Number value = 10;

        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.lt("name", value))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    void shouldSelectWhereNameLte() {
        String documentCollection = "documentCollection";
        Number value = 10;
        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.lte("name", value))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals("name", document.name());
        assertEquals(value, document.get());
    }

    @Test
    void shouldSelectWhereNameBetween() {
        String documentCollection = "documentCollection";
        Number valueA = 10;
        Number valueB = 20;

        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.between("name", Arrays.asList(valueA, valueB)))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();

        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals("name", document.name());
        assertThat(document.get(new TypeReference<List<Number>>() {
        })).contains(10, 20);
    }

    @Test
    void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentQuery query = builder().from(documentCollection).where(eq("name", name).negate())
                .build();
        DocumentCondition condition = query.condition().get();

        Document column = condition.document();
        DocumentCondition negate = column.get(DocumentCondition.class);
        assertTrue(query.documents().isEmpty());
        assertEquals(documentCollection, query.name());
        assertEquals(Condition.NOT, condition.condition());
        assertEquals(Condition.EQUALS, negate.condition());
        assertEquals("name", negate.document().name());
        assertEquals(name, negate.document().get());
    }


    @Test
    void shouldSelectWhereNameAnd() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentCondition nameEqualsAda = eq("name", name);
        DocumentCondition ageOlderTen = DocumentCondition.gt("age", 10);
        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.and(nameEqualsAda, ageOlderTen))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        assertEquals(Condition.AND, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10)));
    }

    @Test
    void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentCondition nameEqualsAda = eq("name", name);
        DocumentCondition ageOlderTen = DocumentCondition.gt("age", 10);
        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.or(nameEqualsAda, ageOlderTen))
                .build();
        DocumentCondition condition = query.condition().get();

        Document document = condition.document();
        List<DocumentCondition> conditions = document.get(new TypeReference<>() {
        });
        assertEquals(Condition.OR, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10)));
    }


    @Test
    void shouldSelectNegate() {
        String collection = "collection";
        DocumentCondition nameNotEqualsLucas = eq("name", "Lucas").negate();
        DocumentQuery query = builder().from(collection)
                .where(nameNotEqualsLucas).build();

        DocumentCondition condition = query.condition().orElseThrow(RuntimeException::new);
        assertEquals(collection, query.name());
        Document column = condition.document();
        List<DocumentCondition> conditions = column.get(new TypeReference<>() {
        });

        assertEquals(Condition.NOT, condition.condition());
        org.assertj.core.api.Assertions.assertThat(conditions).contains(eq(Document.of("name", "Lucas")));

    }


    @Test
    void shouldExecuteManager() {
        DocumentManager manager = Mockito.mock(DocumentManager.class);
        ArgumentCaptor<DefaultDocumentQuery> queryCaptor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        String collection = "collection";
        Stream<DocumentEntity> entities = builder().from(collection).getResult(manager);
        Mockito.verify(manager).select(queryCaptor.capture());
        checkQuery(queryCaptor, collection);
    }

    @Test
    void shouldExecuteSingleResultManager() {
        DocumentManager manager = Mockito.mock(DocumentManager.class);
        ArgumentCaptor<DefaultDocumentQuery> queryCaptor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        String collection = "collection";
        Optional<DocumentEntity> entities = builder().from(collection).getSingleResult(manager);
        Mockito.verify(manager).singleResult(queryCaptor.capture());
        checkQuery(queryCaptor, collection);
    }

    private void checkQuery(ArgumentCaptor<DefaultDocumentQuery> queryCaptor, String collection) {
        DocumentQuery query = queryCaptor.getValue();
        assertTrue(query.documents().isEmpty());
        assertFalse(query.condition().isPresent());
        assertEquals(collection, query.name());
    }
}