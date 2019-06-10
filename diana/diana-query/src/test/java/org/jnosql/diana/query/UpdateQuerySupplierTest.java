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
package org.jnosql.diana.query;

import jakarta.nosql.query.Condition;
import org.jnosql.query.Function;
import org.jnosql.query.FunctionValue;
import org.jnosql.query.InsertQuery;
import org.jnosql.query.JSONValue;
import org.jnosql.query.NumberValue;
import jakarta.nosql.query.Operator;
import org.jnosql.query.ParamValue;
import jakarta.nosql.query.StringValue;
import org.jnosql.query.UpdateQuery;
import org.jnosql.query.UpdateQuerySupplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateQuerySupplierTest {

    private UpdateQuerySupplier update = new AntlrUpdateQueryProvider();


    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> update.apply(null));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (name = \"Diana\")"})
    public void shouldReturnParserQuery(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("name", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof StringValue);
        assertEquals("Diana", StringValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (age = 30)"})
    public void shouldReturnParserQuery1(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("age", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof NumberValue);
        assertEquals(30L, NumberValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (stamina = 32.23)"})
    public void shouldReturnParserQuery2(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("stamina", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof NumberValue);
        assertEquals(32.23, NumberValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (siblings = {\"Apollo\": \"Brother\", \"Zeus\": \"Father\"})"})
    public void shouldReturnParserQuery3(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("siblings", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("Apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (age = @age)"})
    public void shouldReturnParserQuery4(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
        assertEquals(1, conditions.size());
        Condition condition = conditions.get(0);
        assertEquals("age", condition.getName());
        assertEquals(Operator.EQUALS, condition.getOperator());
        Value<?> value = condition.getValue();
        assertTrue(value instanceof ParamValue);
        assertEquals("age", ParamValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (birthday = convert(\"1988-01-01\", java.time.LocalDate))"})
    public void shouldReturnParserQuery5(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
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
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update God (age = 30, name = \"Artemis\")"})
    public void shouldReturnParserQuery6(String query) {
        UpdateQuery updateQuery = checkUpdateFromStart(query);
        List<Condition> conditions = updateQuery.getConditions();
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
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update Person {\"name\":\"Ada Lovelace\"}"})
    public void shouldReturnParserQuery7(String query) {
        UpdateQuery updateQuery = update.apply(query);
        assertEquals("Person", updateQuery.getEntity());
        Assertions.assertTrue(updateQuery.getConditions().isEmpty());
        Assertions.assertTrue(updateQuery.getValue().isPresent());
        JSONValue jsonValue = updateQuery.getValue().get();
        JsonObject jsonObject = jsonValue.get();
        assertEquals("Ada Lovelace", jsonObject.getString("name"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"update Person {\"name\": \"Ada Lovelace\", \"age\": 12, \"sibling\":" +
            " [\"Ana\" ,\"Maria\"]," +
            " \"address\":{\"country\": \"United Kingdom\", \"city\": \"London\"}}"})
    public void shouldReturnParserQuery8(String query) {
        UpdateQuery updateQuery = update.apply(query);
        assertEquals("Person", updateQuery.getEntity());
        Assertions.assertTrue(updateQuery.getConditions().isEmpty());
        Assertions.assertTrue(updateQuery.getValue().isPresent());
        JSONValue jsonValue = updateQuery.getValue().get();
        JsonObject jsonObject = jsonValue.get();
        JsonArray sibling = jsonObject.getJsonArray("sibling");
        JsonObject address = jsonObject.getJsonObject("address");

        assertEquals("Ada Lovelace", jsonObject.getString("name"));
        assertEquals("Ana", sibling.getString(0));
        assertEquals("Maria", sibling.getString(1));
        assertEquals("United Kingdom", address.getString("country"));
        assertEquals("London", address.getString("city"));
    }

    private UpdateQuery checkUpdateFromStart(String query) {
        UpdateQuery updateQuery = update.apply(query);
        assertEquals("God", updateQuery.getEntity());
        return updateQuery;
    }

}
