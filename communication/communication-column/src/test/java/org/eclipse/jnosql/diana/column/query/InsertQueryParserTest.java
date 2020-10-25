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
package org.eclipse.jnosql.diana.column.query;

import jakarta.nosql.QueryException;
import jakarta.nosql.TypeReference;
import jakarta.nosql.column.Column;
import jakarta.nosql.column.ColumnEntity;
import jakarta.nosql.column.ColumnFamilyManager;
import jakarta.nosql.column.ColumnObserverParser;
import jakarta.nosql.column.ColumnPreparedStatement;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InsertQueryParserTest {
    
    private InsertQueryParser parser = new InsertQueryParser();

    private ColumnFamilyManager manager = Mockito.mock(ColumnFamilyManager.class);
    private final ColumnObserverParser observer = new ColumnObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();


        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = 30, name = \"Artemis\")"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Artemis"), entity.find("name").get());
        assertEquals(Column.of("age", 30L), entity.find("age").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 day"})
    public void shouldReturnParserQuery2(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofDays(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 hour"})
    public void shouldReturnParserQuery3(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofHours(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 minute"})
    public void shouldReturnParserQuery4(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofMinutes(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 second"})
    public void shouldReturnParserQuery5(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofSeconds(10L), duration);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 millisecond"})
    public void shouldReturnParserQuery6(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofMillis(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 nanosecond"})
    public void shouldReturnParserQuery7(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofNanos(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"}"})
    public void shouldReturnParserQuery8(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();

        assertEquals("Person", entity.getName());
        assertEquals(Column.of("name", "Ada Lovelace"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 nanosecond"})
    public void shouldReturnParserQuery9(String query) {
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        ColumnEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("Person", entity.getName());
        assertEquals(Column.of("name", "Ada Lovelace"), entity.find("name").get());
        assertEquals(Duration.ofNanos(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\": \"Ada Lovelace\", \"age\": 12, \"sibling\":" +
            " [\"Ana\" ,\"Maria\"]," +
            " \"address\":{\"country\": \"United Kingdom\", \"city\": \"London\"}}"})
    public void shouldReturnParserQuery10(String query) {

        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();
        List<String> siblings = entity.find("sibling").get().get(new TypeReference<List<String>>() {
        });
        List<Column> address = entity.find("address").get().get(new TypeReference<List<Column>>() {
        });
        assertEquals("Person", entity.getName());
        assertEquals(Column.of("name", "Ada Lovelace"), entity.find("name").get());
        assertEquals(Column.of("age", BigDecimal.valueOf(12)), entity.find("age").get());
        assertThat(siblings, contains("Ana", "Maria"));
        assertThat(address, containsInAnyOrder(
                Column.of("country", "United Kingdom"),
                Column.of("city", "London")));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldReturnErrorWhenShouldUsePrepareStatement(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, manager, observer));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldReturnErrorWhenDoesNotBindBeforeExecuteQuery(String query) {

        ColumnPreparedStatement prepare = parser.prepare(query, manager, observer);
        assertThrows(QueryException.class, prepare::getResult);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<ColumnEntity> captor = ArgumentCaptor.forClass(ColumnEntity.class);
        ColumnPreparedStatement prepare = parser.prepare(query, manager, observer);
        prepare.bind("name", "Diana");
        prepare.getResult();
        Mockito.verify(manager).insert(captor.capture());
        ColumnEntity entity = captor.getValue();
        assertEquals("God", entity.getName());
        assertEquals(Column.of("name", "Diana"), entity.find("name").get());

    }

}
