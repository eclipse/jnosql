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
package org.jnosql.diana.api.document.query.parser;

import org.jnosql.diana.api.document.DocumentCollectionManager;
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

import static org.junit.jupiter.api.Assertions.*;


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
        assertEquals(0L, documentQuery.getLimit());
        assertEquals(0L, documentQuery.getSkip());
        assertEquals("God", documentQuery.getDocumentCollection());
        assertFalse(documentQuery.getCondition().isPresent());

    }


}