/*
 *  Copyright (c) 2018 Otávio Santana and others
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *  The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *  and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *  You may elect to redistribute this code under either of these licenses.
 *  Contributors:
 *  Otavio Santana
 */
package org.jnosql.aphrodite.antlr;

import org.jnosql.query.Condition;
import org.jnosql.query.Function;
import org.jnosql.query.FunctionValue;
import org.jnosql.query.InsertQuery;
import org.jnosql.query.InsertQuerySupplier;
import org.jnosql.query.JSONValue;
import org.jnosql.query.NumberValue;
import org.jnosql.query.Operator;
import org.jnosql.query.ParamValue;
import org.jnosql.query.StringValue;
import org.jnosql.query.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.json.JsonObject;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InsertQuerySupplierTest {

    private InsertQuerySupplier insertQuerySupplier = new AntlrInsertQuerySupplier();


    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> insertQuerySupplier.apply(null));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (name = \"Diana\")"})
    public void shouldReturnParserQuery(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("name", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof StringValue);
        assertEquals("Diana", StringValue.class.cast(value).get());
        assertFalse(insertQuery.getTtl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = 30)"})
    public void shouldReturnParserQuery1(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("age", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof NumberValue);
        assertEquals(30L, NumberValue.class.cast(value).get());
        assertFalse(insertQuery.getTtl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (stamina = 32.23)"})
    public void shouldReturnParserQuery2(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("stamina", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof NumberValue);
        assertEquals(32.23, NumberValue.class.cast(value).get());
        assertFalse(insertQuery.getTtl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (siblings = {\"Apollo\": \"Brother\", \"Zeus\": \"Father\"})"})
    public void shouldReturnParserQuery3(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("siblings", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("Apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
        assertFalse(insertQuery.getTtl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = @age)"})
    public void shouldReturnParserQuery4(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("age", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof ParamValue);
        assertEquals("age", ParamValue.class.cast(value).get());
        assertFalse(insertQuery.getTtl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (birthday = convert(\"1988-01-01\", java.time.LocalDate))"})
    public void shouldReturnParserQuery5(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("birthday", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof FunctionValue);
        Function function = FunctionValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals(2, params.length);
        assertEquals("1988-01-01", StringValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
        assertFalse(insertQuery.getTtl().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"insert God (age = 30, name = \"Artemis\")"})
    public void shouldReturnParserQuery6(String query) {
        InsertQuery insertQuery = checkInsertFromStart(query);
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(2, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("age", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof NumberValue);
        assertEquals(30L, NumberValue.class.cast(value).get());

        condition = conditions.get(1);
        assertEquals("name", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        value = condition.getValue();
        assertTrue(value instanceof StringValue);
        assertEquals("Artemis", StringValue.class.cast(value).get());
        assertFalse(insertQuery.getTtl().isPresent());
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


    private void checkTTL(InsertQuery insertQuery, Duration duration) {
        List<Condition> conditions = insertQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("name", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof StringValue);
        assertEquals("Diana", StringValue.class.cast(value).get());

        Optional<Duration> ttl = insertQuery.getTtl();
        assertTrue(ttl.isPresent());
        assertEquals(duration, ttl.get());
    }


    private InsertQuery checkInsertFromStart(String query) {
        InsertQuery insertQuery = insertQuerySupplier.apply(query);
        assertEquals("God", insertQuery.getEntity());
        return insertQuery;
    }

}
