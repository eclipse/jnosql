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

import jakarta.json.JsonObject;
import org.eclipse.jnosql.communication.Condition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteQueryConverterTest {

    private final DeleteQueryConverter selectProvider = new DeleteQueryConverter();

    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> selectProvider.apply(null));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God"})
    public void shouldReturnParserQuery(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertFalse(deleteQuery.where().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete name, address from God"})
    public void shouldReturnParserQuery2(String query) {
        DeleteQuery deleteQuery = selectProvider.apply(query);
        assertEquals("God", deleteQuery.entity());
        assertFalse(deleteQuery.fields().isEmpty());
        assertThat(deleteQuery.fields()).contains("name", "address");
        assertFalse(deleteQuery.where().isPresent());
    }



    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = 10"})
    public void shouldReturnParserQuery3(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10L, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina > 10.23"})
    public void shouldReturnParserQuery4(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals("stamina", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina >= 10.23"})
    public void shouldReturnParserQuery5(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals("stamina", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina <= 10.23"})
    public void shouldReturnParserQuery6(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals("stamina", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina < 10.23"})
    public void shouldReturnParserQuery7(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals("stamina", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age between 10 and 30"})
    public void shouldReturnParserQuery8(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof DefaultArrayQueryValue);
        DefaultArrayQueryValue arrayValue = DefaultArrayQueryValue.class.cast(value);
        QueryValue<?>[] values = arrayValue.get();
        List<Long> ages = Stream.of(values).map(QueryValue::get).map(Long.class::cast).collect(toList());
        assertThat(ages).contains(10L, 30L);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = \"diana\""})
    public void shouldReturnParserQuery9(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("diana", value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = {\"diana\"}"})
    public void shouldReturnParserQuery10(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof DefaultArrayQueryValue);
        List<Object> values = Stream.of(DefaultArrayQueryValue.class.cast(value).get()).map(QueryValue::get)
                .collect(toList());
        assertThat(values).contains("diana");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = {\"diana\", 17, 20.21}"})
    public void shouldReturnParserQuery11(String query) {
        DeleteQuery selectQuery = checkDeleteFromStart(query);
        assertTrue(selectQuery.where().isPresent());

        Where where = selectQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof DefaultArrayQueryValue);
        List<Object> values = Stream.of(DefaultArrayQueryValue.class.cast(value).get()).map(QueryValue::get).collect(toList());
        assertThat(values).contains("diana", 17L, 20.21);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery12(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("siblings", condition.name());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = @name"})
    public void shouldReturnParserQuery13(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);

        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof DefaultQueryValue);
        assertEquals("name", DefaultQueryValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery14(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.name());
        Object[] params = function.params();
        assertEquals(12L, NumberQueryValue.class.cast(params[0]).get());
        assertEquals(Integer.class, params[1]);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery15(String query) {

        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.IN, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof DefaultArrayQueryValue);
        List<Object> values = Stream.of(DefaultArrayQueryValue.class.cast(value).get())
                .map(QueryValue::get).collect(toList());
        assertThat(values).contains("Ada", "Apollo");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name like \"Ada\""})
    public void shouldReturnParserQuery16(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name not like \"Ada\""})
    public void shouldReturnParserQuery17(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.NOT, condition.condition());
        assertEquals("_NOT", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        List<QueryCondition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(1, conditions.size());
        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = \"Ada\" and age = 20 and" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery18(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.AND, condition.condition());
        assertEquals("_AND", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        List<QueryCondition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());
        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("siblings", condition.name());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = \"Ada\" or age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery19(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.OR, condition.condition());
        assertEquals("_OR", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        List<QueryCondition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());
        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("siblings", condition.name());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery20(String query) {
        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.AND, condition.condition());
        assertEquals("_AND", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        List<QueryCondition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());

        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.value();
        Assertions.assertEquals(Condition.OR, condition.condition());

        conditions = ConditionQueryValue.class.cast(condition.value()).get();
        assertEquals(1, conditions.size());

        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());

        assertEquals("siblings", condition.name());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} or birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery21(String query) {

        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.AND, condition.condition());
        assertEquals("_AND", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        List<QueryCondition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());

        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        Assertions.assertEquals(Condition.OR, condition.condition());

        conditions = ConditionQueryValue.class.cast(condition.value()).get();
        assertEquals(2, conditions.size());

        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());

        assertEquals("siblings", condition.name());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));


        condition = conditions.get(1);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());

        assertEquals("birthday", condition.name());
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.name());
        Object[] params = function.params();
        assertEquals("2007-12-03", StringQueryValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery22(String query) {

        DeleteQuery deleteQuery = checkDeleteFromStart(query);
        assertTrue(deleteQuery.where().isPresent());

        Where where = deleteQuery.where().get();
        QueryCondition condition = where.condition();
        QueryValue<?> value = condition.value();
        Assertions.assertEquals(Condition.AND, condition.condition());
        assertEquals("_AND", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        List<QueryCondition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(4, conditions.size());

        condition = conditions.get(0);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", condition.name());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        Assertions.assertEquals(Condition.OR, condition.condition());

        assertEquals(1, ConditionQueryValue.class.cast(condition.value()).get().size());

        QueryCondition c = ConditionQueryValue.class.cast(condition.value()).get().get(0);
        value = c.value();
        Assertions.assertEquals(Condition.EQUALS, c.condition());

        assertEquals("siblings", c.name());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));


        condition = conditions.get(3);
        value = condition.value();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());

        assertEquals("birthday", condition.name());
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.name());
        Object[] params = function.params();
        assertEquals("2007-12-03", StringQueryValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
    }


    private DeleteQuery checkDeleteFromStart(String query) {
        DeleteQuery deleteQuery = selectProvider.apply(query);
        assertEquals("God", deleteQuery.entity());
        assertTrue(deleteQuery.fields().isEmpty());
        return deleteQuery;
    }

}