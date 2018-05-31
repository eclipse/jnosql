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

import org.jnosql.diana.api.Sort;
import org.jnosql.diana.api.document.Document;
import org.jnosql.diana.api.document.DocumentCollectionManager;
import org.jnosql.diana.api.document.DocumentDeleteQuery;
import org.jnosql.diana.api.document.DocumentEntity;
import org.jnosql.diana.api.document.DocumentQuery;
import org.jnosql.diana.api.document.DocumentQueryParser;
import org.jnosql.diana.api.document.query.DefaultDocumentQueryParser;
import org.jnosql.query.QueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.jnosql.diana.api.Sort.SortType.ASC;
import static org.jnosql.diana.api.Sort.SortType.DESC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class DefaultDocumentQueryParserTest {

    private DocumentQueryParser parser = new DefaultDocumentQueryParser();


    private DocumentCollectionManager documentCollection = Mockito.mock(DocumentCollectionManager.class);

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {


        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.query(null, documentCollection);
        });

        Assertions.assertThrows(NullPointerException.class, () -> {
            parser.query("select * from God", null);
        });
    }

    @Test
    public void shouldReturnErrorWhenHasInvalidQuery() {
        Assertions.assertThrows(QueryException.class, () -> {
            parser.query("inva", documentCollection);
        });

        Assertions.assertThrows(QueryException.class, () -> {
            parser.query("invalid", documentCollection);
        });
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<DocumentQuery> captor = ArgumentCaptor.forClass(DocumentQuery.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).select(captor.capture());
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
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).delete(captor.capture());
        DocumentDeleteQuery documentQuery = captor.getValue();

        assertTrue(documentQuery.getDocuments().isEmpty());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).insert(captor.capture());
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<DocumentEntity> captor = ArgumentCaptor.forClass(DocumentEntity.class);
        parser.query(query, documentCollection);
        Mockito.verify(documentCollection).update(captor.capture());
        DocumentEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Document.of("name", "Diana"), entity.find("name").get());
    }



}