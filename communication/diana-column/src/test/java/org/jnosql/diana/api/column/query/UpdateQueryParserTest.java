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
package org.jnosql.diana.api.column.query;

import org.jnosql.diana.api.column.Column;
import org.jnosql.diana.api.column.ColumnEntity;
import org.jnosql.diana.api.column.ColumnFamilyManager;
import org.jnosql.diana.api.column.ColumnFamilyManagerAsync;
import org.jnosql.diana.api.column.ColumnObserverParser;
import org.jnosql.diana.api.column.ColumnPreparedStatement;
import org.jnosql.diana.api.column.ColumnPreparedStatementAsync;
import org.jnosql.query.QueryException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UpdateQueryParserTest {

    private UpdateQueryParser parser = new UpdateQueryParser();

    private ColumnFamilyManager documentCollection = Mockito.mock(ColumnFamilyManager.class);
    private ColumnFamilyManagerAsync documentCollectionAsync = Mockito.mock(ColumnFamilyManagerAsync.class);
    private final ColumnObserverParser observer = new ColumnObserverParser() {
    };

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).update(captor.capture());
        ColumnEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (age = 30, name = \"Artemis\")"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        parser.query(query, documentCollection, observer);
        Mockito.verify(documentCollection).update(captor.capture());
        ColumnEntity entity = captor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Artemis"), entity.find("name").get());
        assertEquals(Column.of("age", 30L), entity.find("age").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldReturnParserQuery8(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, documentCollection, observer));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldReturnErrorWhenDoesNotBindBeforeExecuteQuery(String query) {

        ColumnPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        assertThrows(QueryException.class, () -> prepare.getResultList());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldExecutePrepareStatment(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ColumnPreparedStatement prepare = parser.prepare(query, documentCollection, observer);
        prepare.bind("name", "Diana");
        prepare.getResultList();
        Mockito.verify(documentCollection).update(captor.capture());
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldReturnErrorWhenShouldUsePrepareStatmentAsync(String query) {

        assertThrows(QueryException.class, () -> parser.queryAsync(query, documentCollectionAsync, s->{}, observer));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldReturnErrorWhenDoesNotBindBeforeExecuteQueryAsync(String query) {

        ColumnPreparedStatementAsync prepare = parser.prepareAsync(query, documentCollectionAsync, observer);
        assertThrows(QueryException.class, () -> prepare.getResultList(s ->{}));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldExecutePrepareStatmentAsync(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ColumnPreparedStatementAsync prepare = parser.prepareAsync(query, documentCollectionAsync, observer);
        prepare.bind("name", "Diana");
        Consumer<List<ColumnEntity>> callBack = s -> {
        };
        prepare.getResultList(callBack);
        Mockito.verify(documentCollectionAsync).update(captor.capture(), Mockito.any(Consumer.class));
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());

    }
}