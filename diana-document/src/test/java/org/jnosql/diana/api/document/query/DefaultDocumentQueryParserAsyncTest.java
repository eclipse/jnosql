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
import org.jnosql.diana.api.document.DocumentCollectionManagerAsync;
import org.jnosql.diana.api.document.DocumentCondition;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentPreparedStatementAsync;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.api.document.DocumentQueryParserAsync;
import org.jnosql.query.QueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;

import static org.jnosql.diana.api.Condition.EQUALS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DefaultDocumentQueryParserAsyncTest {

    private DocumentQueryParserAsync parser = new DefaultDocumentQueryParserAsync();


    private DocumentCollectionManagerAsync documentCollection = Mockito.mock(DocumentCollectionManagerAsync.class);

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {


        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.query(null, documentCollection, s -> {
            });
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.query("select * from God", null, s -> {
            });
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.query("select * from God", documentCollection, null);
        });

    }

    @Test
    public void shouldReturnErrorWhenHasInvalidQuery() {

        Assertions.assertThrows(QueryException.class, () -> {
            parser.query("inva", documentCollection, s -> {
            });
        });

        Assertions.assertThrows(QueryException.class, () -> {
            parser.query("invalid", documentCollection, s -> {
            });
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        Consumer<List<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, documentCollection, callBack);
        Mockito.verify(documentCollection).select(captor.capture(), Mockito.eq(callBack));
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
        Consumer<List<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, documentCollection, callBack);
        Mockito.verify(documentCollection).delete(captor.capture(), Mockito.any(Consumer.class));
        DocumentDeleteQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Consumer<List<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, documentCollection, callBack);
        Mockito.verify(documentCollection).insert(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        Consumer<List<DocumentEntity>> callBack = s -> {
        };
        parser.query(query, documentCollection, callBack);
        Mockito.verify(documentCollection).update(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatment(String query) {
        ArgumentCaptor<DocumentDeleteQuery> captor = ArgumentCaptor.forClass(DocumentDeleteQuery.class);


        DocumentPreparedStatementAsync prepare = parser.prepare(query, documentCollection);
        prepare.bind("age", 12);
        prepare.getResultList(s -> {
        });
        Mockito.verify(documentCollection).delete(captor.capture(), Mockito.any(Consumer.class));
        DocumentDeleteQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldExecutePrepareStatment1(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        DocumentPreparedStatementAsync prepare = parser.prepare(query, documentCollection);
        prepare.bind("name", "Diana");
        prepare.getResultList(s -> {
        });
        Mockito.verify(documentCollection).insert(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatment2(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);

        DocumentPreparedStatementAsync prepare = parser.prepare(query, documentCollection);
        prepare.bind("age", 12);
        prepare.getResultList(s -> {
        });
        Mockito.verify(documentCollection).select(captor.capture(), Mockito.any(Consumer.class));
        DocumentQuery documentQuery = captor.getValue();
        DocumentCondition documentCondition = documentQuery.getCondition().get();
        Document document = documentCondition.getDocument();
        assertEquals(EQUALS, documentCondition.getCondition());
        assertEquals("age", document.getName());
        assertEquals(12, document.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldExecutePrepareStatment3(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        DocumentPreparedStatementAsync prepare = parser.prepare(query, documentCollection);
        prepare.bind("name", "Diana");
        prepare.getResultList(s -> {
        });
        Mockito.verify(documentCollection).update(captor.capture(), Mockito.any(Consumer.class));
        DocumentEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());

    }


}