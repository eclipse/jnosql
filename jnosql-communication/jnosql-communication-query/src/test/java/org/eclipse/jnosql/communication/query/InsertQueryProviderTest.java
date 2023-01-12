/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.eclipse.jnosql.communication.query;

import org.eclipse.jnosql.communication.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsertQueryProviderTest {

    private final InsertQueryProvider insertQueryProvider = new InsertQueryProvider();


    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> insertQueryProvider.apply(null));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("name", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Diana", StringQueryValue.class.cast(value).get());
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = 30)"})
    public void shouldReturnParserQuery1(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("age", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(30L, NumberQueryValue.class.cast(value).get());
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (stamina = 32.23)"})
    public void shouldReturnParserQuery2(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("stamina", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(32.23, NumberQueryValue.class.cast(value).get());
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (siblings = {\"Apollo\": \"Brother\", \"Zeus\": \"Father\"})"})
    public void shouldReturnParserQuery3(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("siblings", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("Apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = @age)"})
    public void shouldReturnParserQuery4(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("age", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof DefaultQueryValue);
        assertEquals("age", DefaultQueryValue.class.cast(value).get());
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (birthday = convert(\"1988-01-01\", java.time.LocalDate))"})
    public void shouldReturnParserQuery5(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("birthday", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.name());
        Object[] params = function.params();
        assertEquals(2, params.length);
        assertEquals("1988-01-01", StringQueryValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = 30, name = \"Artemis\")"})
    public void shouldReturnParserQuery6(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(2, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("age", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(30L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        assertEquals("name", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        value = condition.value();
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Artemis", StringQueryValue.class.cast(value).get());
        assertFalse(insertQuery.ttl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 day"})
    public void shouldReturnParserQuery7(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofDays(10L));

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 hour"})
    public void shouldReturnParserQuery8(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofHours(10L));

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 minute"})
    public void shouldReturnParserQuery9(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofMinutes(10L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 second"})
    public void shouldReturnParserQuery10(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofSeconds(10L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 millisecond"})
    public void shouldReturnParserQuery11(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofMillis(10L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\") 10 nanosecond"})
    public void shouldReturnParserQuery12(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofNanos(10L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = 'Diana') 10 nanosecond"})
    public void shouldReturnParserQuery13(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        checkTTL(insertQuery, Duration.ofNanos(10L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"}"})
    public void shouldReturnParserQuery14(String query) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);
        assertEquals("Person", insertQuery.entity());
        Assertions.assertTrue(insertQuery.conditions().isEmpty());
        Assertions.assertTrue(insertQuery.value().isPresent());
        JSONQueryValue JSONQueryValue = insertQuery.value().get();
        JsonObject jsonObject = JSONQueryValue.get();
        assertEquals("Ada Lovelace", jsonObject.getString("name"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 day"})
    public void shouldReturnParserQuery15(String query) {
        Duration duration = Duration.ofDays(10);
        checkJSONInsertQuery(query, duration);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 hour"})
    public void shouldReturnParserQuery16(String query) {
        Duration duration = Duration.ofHours(10);
        checkJSONInsertQuery(query, duration);
    }
    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 minute"})
    public void shouldReturnParserQuery17(String query) {
        Duration duration = Duration.ofMinutes(10);
        checkJSONInsertQuery(query, duration);
    }
    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 second"})
    public void shouldReturnParserQuery18(String query) {
        Duration duration = Duration.ofSeconds(10);
        checkJSONInsertQuery(query, duration);
    }
    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 millisecond"})
    public void shouldReturnParserQuery19(String query) {
        Duration duration = Duration.ofMillis(10);
        checkJSONInsertQuery(query, duration);
    }
    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\":\"Ada Lovelace\"} 10 nanosecond"})
    public void shouldReturnParserQuery20(String query) {
        Duration duration = Duration.ofNanos(10);
        checkJSONInsertQuery(query, duration);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert Person {\"name\": \"Ada Lovelace\", \"age\": 12, \"sibling\":" +
            " [\"Ana\" ,\"Maria\"]," +
            " \"address\":{\"country\": \"United Kingdom\", \"city\": \"London\"}}"})
    public void shouldReturnParserQuery21(String query) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);
        assertEquals("Person", insertQuery.entity());
        Assertions.assertTrue(insertQuery.conditions().isEmpty());
        Assertions.assertTrue(insertQuery.value().isPresent());
        JSONQueryValue JSONQueryValue = insertQuery.value().get();
        JsonObject jsonObject = JSONQueryValue.get();
        JsonArray sibling = jsonObject.getJsonArray("sibling");
        JsonObject address = jsonObject.getJsonObject("address");

        assertEquals("Ada Lovelace", jsonObject.getString("name"));
        assertEquals("Ana", sibling.getString(0));
        assertEquals("Maria", sibling.getString(1));
        assertEquals("United Kingdom", address.getString("country"));
        assertEquals("London", address.getString("city"));
    }


    private void checkJSONInsertQuery(String query, Duration duration) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);
        assertEquals("Person", insertQuery.entity());
        Assertions.assertTrue(insertQuery.conditions().isEmpty());
        Assertions.assertTrue(insertQuery.value().isPresent());
        JSONQueryValue JSONQueryValue = insertQuery.value().get();
        JsonObject jsonObject = JSONQueryValue.get();
        assertEquals("Ada Lovelace", jsonObject.getString("name"));
        assertEquals(duration, insertQuery.ttl().get());
    }


    private void checkTTL(InsertQuery insertQuery, Duration duration) {
        List<QueryCondition> conditions = insertQuery.conditions();
        assertEquals(1, conditions.size());
        QueryCondition condition = conditions.get(0);
        assertEquals("name", condition.name());
        assertEquals(Condition.EQUALS, condition.condition());
        QueryValue<?> value = condition.value();
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Diana", StringQueryValue.class.cast(value).get());

        Optional<Duration> ttl = insertQuery.ttl();
        assertTrue(ttl.isPresent());
        assertEquals(duration, ttl.get());
    }


    private InsertQuery checkInsertFromStart(String query) {
        InsertQuery insertQuery = insertQueryProvider.apply(query);
        assertEquals("God", insertQuery.entity());
        return insertQuery;
    }

}
