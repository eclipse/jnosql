/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.diana.column.query;

import jakarta.nosql.Condition;
import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.QueryException;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnCondition;
import jakarta.nosql.column.ColumnDeleteQuery;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManagerAsync;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnPreparedStatementAsync;
import jakarta.nosql.column.ColumnQuery;
import jakarta.nosql.column.ColumnQueryParserAsync;
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

class DefaultColumnQueryParserAsyncTest {

    private ColumnQueryParserAsync parser = new DefaultColumnQueryParserAsync();

    private ColumnFamilyManagerAsync manager = Mockito.mock(ColumnFamilyManagerAsync.class);

    @Test
    public void shouldReturnNPEWhenThereIsNullParameter() {
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        assertThrows(NullPointerException.class, () -> parser.query(null, manager, callBack, ColumnObserverParser.EMPTY));
        assertThrows(NullPointerException.class, () -> parser.query("select * from God", null, callBack,
                ColumnObserverParser.EMPTY));
    }

    @Test
    public void shouldReturnErrorWhenHasInvalidQuery() {

        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        assertThrows(QueryException.class, () -> parser.query("inva", manager, callBack, ColumnObserverParser.EMPTY));
        assertThrows(QueryException.class, () -> parser.query("invalid", manager, callBack, ColumnObserverParser.EMPTY));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        ArgumentCaptor<Consumer<Stream<ColumnEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        parser.query(query, manager, callBack, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).select(captor.capture(), callBackCaptor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.getColumns().isEmpty());
        assertTrue(columnQuery.getSorts().isEmpty());
        assertEquals(0L, columnQuery.getLimit());
        assertEquals(0L, columnQuery.getSkip());
        assertEquals("God", columnQuery.getColumnFamily());
        assertFalse(columnQuery.getCondition().isPresent());

    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        ArgumentCaptor<Consumer<Void>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        parser.query(query, manager, callBack, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).delete(captor.capture(), callBackCaptor.capture());
        ColumnDeleteQuery columnDeleteQuery = captor.getValue();

        assertTrue(columnDeleteQuery.getColumns().isEmpty());
        assertEquals("God", columnDeleteQuery.getColumnFamily());
        assertFalse(columnDeleteQuery.getCondition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery2(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        ArgumentCaptor<Consumer<ColumnEntity>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        parser.query(query, manager, callBack, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).insert(captor.capture(), callBackCaptor.capture());
        ColumnEntity entity = captor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        ArgumentCaptor<Consumer<ColumnEntity>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        parser.query(query, manager, callBack, ColumnObserverParser.EMPTY);
        Mockito.verify(manager).update(captor.capture(), callBackCaptor.capture());
        ColumnEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<ColumnDeleteQuery> captor = ArgumentCaptor.forClass(ColumnDeleteQuery.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        ArgumentCaptor<Consumer<Void>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult(callBack);
        Mockito.verify(manager).delete(captor.capture(), callBackCaptor.capture());
        ColumnDeleteQuery columnDeleteQuery = captor.getValue();
        ColumnCondition columnCondition = columnDeleteQuery.getCondition().get();
        Column column = columnCondition.getColumn();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldExecutePrepareStatement1(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Consumer<ColumnEntity>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };

        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult(callBack);
        Mockito.verify(manager).insert(captor.capture(), callBackCaptor.capture());
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement2(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };

        ArgumentCaptor<Consumer<Stream<ColumnEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("age", 12);
        prepare.getResult(callBack);
        Mockito.verify(manager).select(captor.capture(), callBackCaptor.capture());
        ColumnQuery columnQuery = captor.getValue();
        ColumnCondition columnCondition = columnQuery.getCondition().get();
        Column column = columnCondition.getColumn();
        assertEquals(Condition.EQUALS, columnCondition.getCondition());
        assertEquals("age", column.getName());
        assertEquals(12, column.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = @name)"})
    public void shouldExecutePrepareStatement3(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        Consumer<Stream<ColumnEntity>> callBack = (c) -> {
        };
        ArgumentCaptor<Consumer<ColumnEntity>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);
        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getResult(callBack);
        Mockito.verify(manager).update(captor.capture(), callBackCaptor.capture());
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldSingleResult(String query) {
        AtomicReference<Optional<ColumnEntity>> reference = new AtomicReference<>();
        Consumer<Optional<ColumnEntity>> callBack = reference::set;
        ArgumentCaptor<Consumer<Stream<ColumnEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);

        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getSingleResult(callBack);
        Mockito.verify(manager).select(Mockito.any(ColumnQuery.class), callBackCaptor.capture());
        final Consumer<Stream<ColumnEntity>> consumer = callBackCaptor.getValue();
        consumer.accept(Stream.of(Mockito.mock(ColumnEntity.class)));
        final Optional<ColumnEntity> columnEntity = reference.get();
        assertTrue(columnEntity.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldReturnEmptySingleResult(String query) {
        AtomicReference<Optional<ColumnEntity>> reference = new AtomicReference<>();
        Consumer<Optional<ColumnEntity>> callBack = reference::set;
        ArgumentCaptor<Consumer<Stream<ColumnEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);

        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getSingleResult(callBack);
        Mockito.verify(manager).select(Mockito.any(ColumnQuery.class), callBackCaptor.capture());
        final Consumer<Stream<ColumnEntity>> consumer = callBackCaptor.getValue();
        consumer.accept(Stream.empty());
        final Optional<ColumnEntity> columnEntity = reference.get();
        assertFalse(columnEntity.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldReturnErrorSingleResult(String query) {
        AtomicReference<Optional<ColumnEntity>> reference = new AtomicReference<>();
        Consumer<Optional<ColumnEntity>> callBack = reference::set;
        ArgumentCaptor<Consumer<Stream<ColumnEntity>>> callBackCaptor = ArgumentCaptor.forClass(Consumer.class);

        ColumnPreparedStatementAsync prepare = parser.prepare(query, manager, ColumnObserverParser.EMPTY);
        prepare.bind("name", "Diana");
        prepare.getSingleResult(callBack);
        Mockito.verify(manager).select(Mockito.any(ColumnQuery.class), callBackCaptor.capture());
        final Consumer<Stream<ColumnEntity>> consumer = callBackCaptor.getValue();
       assertThrows(NonUniqueResultException.class, () ->
               consumer.accept(Stream.of(Mockito.mock(ColumnEntity.class), Mockito.mock(ColumnEntity.class))));
    }
}