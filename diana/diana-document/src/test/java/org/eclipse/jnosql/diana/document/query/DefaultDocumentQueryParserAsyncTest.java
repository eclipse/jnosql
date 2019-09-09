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
package org.eclipse.jnosql.diana.document.query;

import jakarta.nosql.Condition;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.document.Document;
import jakarta.nosql.document.DocumentCollectionManagerAsync;
import jakarta.nosql.document.DocumentCondition;
import jakarta.nosql.document.DocumentDeleteQuery;
import jakarta.nosql.document.DocumentEntity;
import jakarta.nosql.document.DocumentObserverParser;
import jakarta.nosql.document.DocumentPreparedStatementAsync;
import jakarta.nosql.document.DocumentQuery;
import jakarta.nosql.document.DocumentQueryParserAsync;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DefaultDocumentQueryParserAsyncTest {

    private DocumentQueryParserAsync parser = new DefaultDocumentQueryParserAsync();


    private DocumentCollectionManagerAsync manager = Mockito.mock(DocumentCollectionManagerAsync.class);

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {


        Assertions.assertThrows(NullPointerException.class, () -> parser.query(null, manager, s -> {
        }, DocumentObserverParser.EMPTY));

        Assertions.assertThrows(NullPointerException.class, () -> parser.query("select * from God", null, s -> {
        }, DocumentObserverParser.EMPTY));

        Assertions.assertThrows(NullPointerException.class, () -> parser.query("select * from God", manager, null, DocumentObserverParser.EMPTY));

    }

    @Test
    public void shouldReturnErrorWhenHasInvalidQuery() {

        Assertions.assertThrows(QueryException.class, () -> parser.query("inva", manager, s -> {
        }, DocumentObserverParser.EMPTY));

        Assertions.assertThrows(QueryException.class, () -> parser.query("invalid", manager, s -> {
        }, DocumentObserverParser.EMPTY));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        Consumer<Stream<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, manager, callBack, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).select(captor.capture(), Mockito.eq(callBack));
        DocumentQuery documentQuery = captor.getValue();

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
        Consumer<Stream<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, manager, callBack, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).delete(captor.capture(), Mockito.any(Consumer.class));
        DocumentDeleteQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Consumer<Stream<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, manager, callBack, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).insert(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Consumer<Stream<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, manager, callBack, DocumentObserverParser.EMPTY);
        Mockito.verify(manager).update(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);


        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult(s -> {
        });
        Mockito.verify(manager).delete(captor.capture(), Mockito.any(Consumer.class));
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
        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult(s -> {
        });
        Mockito.verify(manager).insert(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement2(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);

        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult(s -> {
        });
        Mockito.verify(manager).select(captor.capture(), Mockito.any(Consumer.class));
        DocumentQuery documentQuery = captor.getValue();
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
        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult(s -> {
        });
        Mockito.verify(manager).update(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldSingleResult(String query) {
        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> callBack = reference::set;
        ArgumentCaptor<Consumer<Stream<DocumentEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);

        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getSingleResult(callBack);
        Mockito.verify(manager).select(Mockito.any(DocumentQuery.class), callBackCaptor.capture());
        final Consumer<Stream<DocumentEntity>> consumer = callBackCaptor.getValue();
        consumer.accept(Stream.of(Mockito.mock(DocumentEntity.class)));
        final Optional<DocumentEntity> columnEntity = reference.get();
        assertTrue(columnEntity.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldReturnEmptySingleResult(String query) {
        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> callBack = reference::set;
        ArgumentCaptor<Consumer<Stream<DocumentEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);

        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getSingleResult(callBack);
        Mockito.verify(manager).select(Mockito.any(DocumentQuery.class), callBackCaptor.capture());
        final Consumer<Stream<DocumentEntity>> consumer = callBackCaptor.getValue();
        consumer.accept(Stream.empty());
        final Optional<DocumentEntity> columnEntity = reference.get();
        assertFalse(columnEntity.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldReturnErrorSingleResult(String query) {
        AtomicReference<Optional<DocumentEntity>> reference = new AtomicReference<>();
        Consumer<Optional<DocumentEntity>> callBack = reference::set;
        ArgumentCaptor<Consumer<Stream<DocumentEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);

        DocumentPreparedStatementAsync prepare = parser.prepare(query, manager, DocumentObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getSingleResult(callBack);
        Mockito.verify(manager).select(Mockito.any(DocumentQuery.class), callBackCaptor.capture());
        final Consumer<Stream<DocumentEntity>> consumer = callBackCaptor.getValue();
        assertThrows(NonUniqueResultException.class, () ->
                consumer.accept(Stream.of(Mockito.mock(DocumentEntity.class), Mockito.mock(DocumentEntity.class))));
    }
}