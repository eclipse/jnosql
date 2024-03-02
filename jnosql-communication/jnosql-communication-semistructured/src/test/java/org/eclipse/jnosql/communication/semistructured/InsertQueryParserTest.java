/*
 *  Copyright (c) 2024 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 * and Apache License v2.0 which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 * You may elect to redistribute this code under either of these licenses.
 *
 */
package org.eclipse.jnosql.communication.semistructured;

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.TypeReference;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InsertQueryParserTest {
    
    private final InsertQueryParser parser = new InsertQueryParser();

    private final DatabaseManager manager = Mockito.mock(DatabaseManager.class);

    private final CommunicationObserverParser observer = new CommunicationObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    void shouldReturnParserQuery(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();


        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = 30, name = \"Artemis\")"})
    void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Artemis"), entity.find("name").get());
        assertEquals(Element.of("age", 30L), entity.find("age").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 day"})
    void shouldReturnParserQuery2(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofDays(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 hour"})
    void shouldReturnParserQuery3(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofHours(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 minute"})
    void shouldReturnParserQuery4(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofMinutes(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 second"})
    void shouldReturnParserQuery5(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofSeconds(10L), duration);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 millisecond"})
    void shouldReturnParserQuery6(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofMillis(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 nanosecond"})
    void shouldReturnParserQuery7(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());
        assertEquals(Duration.ofNanos(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"}"})
    void shouldReturnParserQuery8(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();

        assertEquals("Person", entity.name());
        assertEquals(Element.of("name", "Ada Lovelace"), entity.find("name").get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 nanosecond"})
    void shouldReturnParserQuery9(String query) {
        ArgumentCaptor<Duration> durationCaptor = ArgumentCaptor.forClass(Duration.class);
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture(), durationCaptor.capture());
        CommunicationEntity entity = captor.getValue();
        Duration duration = durationCaptor.getValue();

        assertEquals("Person", entity.name());
        assertEquals(Element.of("name", "Ada Lovelace"), entity.find("name").get());
        assertEquals(Duration.ofNanos(10L), duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\": \"Ada Lovelace\", \"age\": 12, \"sibling\":" +
            " [\"Ana\" ,\"Maria\"]," +
            " \"address\":{\"country\": \"United Kingdom\", \"city\": \"London\"}}"})
    void shouldReturnParserQuery10(String query) {

        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);

        parser.query(query, manager, observer);
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();
        List<String> siblings = entity.find("sibling").get().get(new TypeReference<>() {
        });
        List<Element> address = entity.find("address").get().get(new TypeReference<>() {
        });
        assertEquals("Person", entity.name());
        assertEquals(Element.of("name", "Ada Lovelace"), entity.find("name").get());
        assertEquals(Element.of("age", BigDecimal.valueOf(12)), entity.find("age").get());
        assertThat(siblings).contains("Ana", "Maria");
        Assertions.assertThat(address).contains(
                Element.of("country", "United Kingdom"),
                Element.of("city", "London"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    void shouldReturnErrorWhenShouldUsePrepareStatement(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, manager, observer));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    void shouldReturnErrorWhenDoesNotBindBeforeExecuteQuery(String query) {

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, observer);
        assertThrows(QueryException.class, prepare::result);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = @name)"})
    void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<CommunicationEntity> captor = ArgumentCaptor.forClass(CommunicationEntity.class);
        CommunicationPreparedStatement prepare = parser.prepare(query, manager, observer);
        prepare.bind("name", "Diana");
        prepare.result();
        Mockito.verify(manager).insert(captor.capture());
        CommunicationEntity entity = captor.getValue();
        assertEquals("God", entity.name());
        assertEquals(Element.of("name", "Diana"), entity.find("name").get());

    }

}
