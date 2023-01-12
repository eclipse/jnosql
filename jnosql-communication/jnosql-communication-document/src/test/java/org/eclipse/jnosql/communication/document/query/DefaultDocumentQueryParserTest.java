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
package org.eclipse.jnosql.communication.document.query;

import jakarta.nosql.Condition;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentManager;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentPreparedStatement;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.document.DocumentQueryParser;
import org.eclipse.jnosql.communication.document.DefaultDocumentQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;


class DefaultDocumentQueryParserTest {

    private final DocumentQueryParser parser = new DefaultDocumentQueryParser();

    private final DocumentManager manager = Mockito.mock(DocumentManager.class);

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {

        assertThrows(NullPointerException.class, () -> parser.query(null, manager, DocumentObserverParser.EMPTY));
        assertThrows(NullPointerException.class, () -> parser.query("select * from God", null, DocumentObserverParser.EMPTY));
    }

    @Test
    public void shouldReturnErrorWhenHasInvalidQuery() {

        assertThrows(QueryException.class, () -> parser.query("inva", manager, DocumentObserverParser.EMPTY));
        assertThrows(QueryException.class, () -> parser.query("invalid", manager, DocumentObserverParser.EMPTY));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);
        parser.query(query, manager, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertTrue(documentQuery.getSorts().isEmpty());
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());

    }


    @ParameterizedTest(name = "Should parser the query {0}")
        @ValueSource(strings = {"delete from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);
        parser.query(query, manager, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        parser.query(query, manager, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).insert(captor.capture());
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        parser.query(query, manager, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).update(captor.capture());
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);

        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult();
        Mockito.verify(manager).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(Condition.EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldExecutePrepareStatement1(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult();
        Mockito.verify(manager).insert(captor.capture());
        DocumentEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement2(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);

        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult();
        Mockito.verify(manager).select(captor.capture());
        DefaultDocumentQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(Condition.EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldExecutePrepareStatement3(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult();
        Mockito.verify(manager).update(captor.capture());
        DocumentEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldSingleResult(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);

        Mockito.when(manager.select(Mockito.any(DefaultDocumentQuery.class)))
                .thenReturn(Stream.of(mock(DocumentEntity.class)));

        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        final Optional<DocumentEntity> result = prepare.getSingleResult();
        Mockito.verify(manager).select(captor.capture());
        DefaultDocumentQuery columnQuery = captor.getValue();
        DocumentCondition columnCondition = columnQuery.getCondition().get();
        Document column = columnCondition.getDocument();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
        assertTrue(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnEmptySingleResult(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);

        Mockito.when(manager.select(Mockito.any(DefaultDocumentQuery.class)))
                .thenReturn(Stream.empty());

        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        final Optional<DocumentEntity> result = prepare.getSingleResult();
        Mockito.verify(manager).select(captor.capture());
        DefaultDocumentQuery columnQuery = captor.getValue();
        DocumentCondition columnCondition = columnQuery.getCondition().get();
        Document column = columnCondition.getDocument();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
        assertFalse(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnErrorSingleResult(String query) {
        ArgumentCaptor<DefaultDocumentQuery> captor = ArgumentCaptor.forClass(DefaultDocumentQuery.class);

        Mockito.when(manager.select(Mockito.any(DefaultDocumentQuery.class)))
                .thenReturn(Stream.of(mock(DocumentEntity.class), mock(DocumentEntity.class)));

        DocumentPreparedStatement prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        assertThrows(NonUniqueResultException.class, prepare::getSingleResult);
    }

}