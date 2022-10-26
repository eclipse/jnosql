/*
 *
 *  Copyright (c) 2022 Otávio Santana and others
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
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.Condition;
import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.TypeReference;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManager;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static jakarta.nosql.document.DocumentCondition.eq;
import static jakarta.nosql.document.DocumentQuery.builder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DefaultDocumentQueryBuilderTest {
    @Test
    public void shouldReturnErrorWhenHasNullElementInSelect() {
        assertThrows(NullPointerException.class, () -> builder("document", "document'", null));
    }

    @Test
    public void shouldBuilder() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }

    @Test
    public void shouldSelectDocument() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder("document", "document2").from(documentCollection).build();
        assertThat(query.getDocuments()).contains("document", "document2");
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
    }

    @Test
    public void shouldReturnErrorWhenFromIsNull() {
        assertThrows(NullPointerException.class, () -> builder().from(null));
    }


    @Test
    public void shouldSelectOrderAsc() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).sort(Sort.asc("name")).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertThat(query.getSorts()).contains(Sort.of("name", SortType.ASC));
    }

    @Test
    public void shouldSelectOrderDesc() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).sort(Sort.desc("name")).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertThat(query.getSorts()).contains(Sort.of("name", SortType.DESC));
    }


    @Test
    public void shouldReturnErrorSelectWhenOrderIsNull() {
        assertThrows(NullPointerException.class,() -> {
            String documentCollection = "documentCollection";
            builder().from(documentCollection).sort((Sort) null);
        });
    }

    @Test
    public void shouldSelectLimit() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).limit(10).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(10L, query.getLimit());
    }

    @Test
    public void shouldReturnErrorWhenLimitIsNegative() {
        String documentCollection = "documentCollection";
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder().from(documentCollection).limit(-1));
    }

    @Test
    public void shouldSelectSkip() {
        String documentCollection = "documentCollection";
        DocumentQuery query = builder().from(documentCollection).skip(10).build();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(10L, query.getSkip());
    }

    @Test
    public void shouldReturnErrorWhenSkipIsNegative() {
        String documentCollection = "documentCollection";
        Assertions.assertThrows(IllegalArgumentException.class, () -> builder().from(documentCollection).skip(-1));
    }

    @Test
    public void shouldSelectWhereNameEq() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.eq("name", name))
                .build();
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
        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.like("name", name))
                .build();
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

        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.gt("name", 10))
                .build();

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
        DocumentCondition gteName = DocumentCondition.gte("name", value);
        DocumentQuery query = builder().from(documentCollection).where(gteName).build();
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

        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.lt("name", value))
                .build();
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
        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.lte("name", value))
                .build();
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

        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.between("name", Arrays.asList(valueA, valueB)))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();

        assertTrue(query.getDocuments().isEmpty());
        assertEquals(documentCollection, query.getDocumentCollection());
        assertEquals(Condition.BETWEEN, condition.getCondition());
        assertEquals("name", document.getName());
        assertThat(document.get(new TypeReference<List<Number>>() {
        })).contains(10, 20);
    }

    @Test
    public void shouldSelectWhereNameNot() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";

        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.eq("name", name).negate())
                .build();
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
        DocumentCondition nameEqualsAda = DocumentCondition.eq("name", name);
        DocumentCondition ageOlderTen = DocumentCondition.gt("age", 10);
        DocumentQuery query = builder().from(documentCollection)
                .where(DocumentCondition.and(nameEqualsAda, ageOlderTen))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.AND, condition.getCondition());
        assertThat(conditions).contains(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10)));
    }

    @Test
    public void shouldSelectWhereNameOr() {
        String documentCollection = "documentCollection";
        String name = "Ada Lovelace";
        DocumentCondition nameEqualsAda = DocumentCondition.eq("name", name);
        DocumentCondition ageOlderTen = DocumentCondition.gt("age", 10);
        DocumentQuery query = builder().from(documentCollection).where(DocumentCondition.or(nameEqualsAda, ageOlderTen))
                .build();
        DocumentCondition condition = query.getCondition().get();

        Document document = condition.getDocument();
        List<DocumentCondition> conditions = document.get(new TypeReference<List<DocumentCondition>>() {
        });
        assertEquals(Condition.OR, condition.getCondition());
        assertThat(conditions).contains(eq(Document.of("name", name)),
                DocumentCondition.gt(Document.of("age", 10)));
    }


    @Test
    public void shouldSelectNegate() {
        String collection = "collection";
        DocumentCondition nameNotEqualsLucas = DocumentCondition.eq("name", "Lucas").negate();
        DocumentQuery query = builder().from(collection)
                .where(nameNotEqualsLucas).build();

        DocumentCondition condition = query.getCondition().orElseThrow(RuntimeException::new);
        assertEquals(collection, query.getDocumentCollection());
        Document column = condition.getDocument();
        List<DocumentCondition> conditions = column.get(new TypeReference<List<DocumentCondition>>() {
        });

        assertEquals(Condition.NOT, condition.getCondition());
        assertThat(conditions).contains(eq(Document.of("name", "Lucas")));

    }


    @Test
    public void shouldExecuteManager() {
        DocumentCollectionManager manager = Mockito.mock(DocumentCollectionManager.class);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        String collection = "collection";
        Stream<DocumentEntity> entities = builder().from(collection).getResult(manager);
        Mockito.verify(manager).select(queryCaptor.capture());
        checkQuery(queryCaptor, collection);
    }

    @Test
    public void shouldExecuteSingleResultManager() {
        DocumentCollectionManager manager = Mockito.mock(DocumentCollectionManager.class);
        ArgumentCaptor<DocumentQuery> queryCaptor = ArgumentCaptor.forClass(DocumentQuery.class);
        String collection = "collection";
        Optional<DocumentEntity> entities = builder().from(collection).getSingleResult(manager);
        Mockito.verify(manager).singleResult(queryCaptor.capture());
        checkQuery(queryCaptor, collection);
    }

    private void checkQuery(ArgumentCaptor<DocumentQuery> queryCaptor, String collection) {
        DocumentQuery query = queryCaptor.getValue();
        assertTrue(query.getDocuments().isEmpty());
        assertFalse(query.getCondition().isPresent());
        assertEquals(collection, query.getDocumentCollection());
    }
}