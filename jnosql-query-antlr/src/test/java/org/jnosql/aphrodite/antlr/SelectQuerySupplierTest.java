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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jnosql.query.ArrayValue;
import org.jnosql.query.Condition;
import org.jnosql.query.ConditionValue;
import org.jnosql.query.Function;
import org.jnosql.query.FunctionValue;
import org.jnosql.query.JSONValue;
import org.jnosql.query.NumberValue;
import org.jnosql.query.ParamValue;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.SelectQuerySupplier;
import org.jnosql.query.Sort;
import org.jnosql.query.StringValue;
import org.jnosql.query.Value;
import org.jnosql.query.Where;
import org.jnosql.query.Operator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.json.JsonObject;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SelectQuerySupplierTest {

    private SelectQuerySupplier selectQuerySupplier = new AntlrSelectQuerySupplier();

    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> selectQuerySupplier.apply(null));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God"})
    public void shouldReturnParserQuery(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God"})
    public void shouldReturnParserQuery2(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God order by name"})
    public void shouldReturnParserQuery3(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertFalse(selectQuery.getOrderBy().isEmpty());
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getName).collect(toList()), contains("name"));
        MatcherAssert.assertThat(selectQuery.getOrderBy().stream().map(Sort::getType).collect(toList()), Matchers.contains(Sort.SortType.ASC));
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God order by name desc"})
    public void shouldReturnParserQuery4(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertFalse(selectQuery.getOrderBy().isEmpty());
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getName).collect(toList()), contains("name"));
        MatcherAssert.assertThat(selectQuery.getOrderBy().stream().map(Sort::getType).collect(toList()), Matchers.contains(Sort.SortType.DESC));
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God order by name desc age asc"})
    public void shouldReturnParserQuery5(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertFalse(selectQuery.getOrderBy().isEmpty());
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getName).collect(toList()), contains("name", "age"));
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getType).collect(toList()), Matchers.contains(Sort.SortType.DESC, Sort.SortType.ASC));
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 12"})
    public void shouldReturnParserQuery6(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(12, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God limit 12"})
    public void shouldReturnParserQuery7(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(12, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 10 limit 12"})
    public void shouldReturnParserQuery8(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(12, selectQuery.getLimit());
        assertEquals(10, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = 10"})
    public void shouldReturnParserQuery9(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(10L, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina > 10.23"})
    public void shouldReturnParserQuery10(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.GREATER_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina >= 10.23"})
    public void shouldReturnParserQuery11(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.GREATER_EQUALS_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina <= 10.23"})
    public void shouldReturnParserQuery12(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.LESSER_EQUALS_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina < 10.23"})
    public void shouldReturnParserQuery13(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.LESSER_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(10.23, value.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age between 10 and 30"})
    public void shouldReturnParserQuery14(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.BETWEEN, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof ArrayValue);
        ArrayValue arrayValue = ArrayValue.class.cast(value);
        Value<?>[] values = arrayValue.get();
        assertThat(Stream.of(values).map(Value::get).collect(toList()), contains(10L, 30L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"diana\""})
    public void shouldReturnParserQuery15(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("diana", value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = 'diana'"})
    public void shouldReturnParserQuery16(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("diana", value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = {\"diana\"}"})
    public void shouldReturnParserQuery17(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ArrayValue);
        List<?> values = Stream.of(ArrayValue.class.cast(value).get()).map(Value::get).collect(toList());
        assertThat(values, contains("diana"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = {\"diana\", 17, 20.21}"})
    public void shouldReturnParserQuery18(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ArrayValue);
        List<?> values = Stream.of(ArrayValue.class.cast(value).get()).map(Value::get).collect(toList());
        assertThat(values, contains("diana", 17L, 20.21));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery19(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = @name"})
    public void shouldReturnParserQuery20(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ParamValue);
        assertEquals("name", ParamValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery21(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof FunctionValue);
        Function function = FunctionValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals(12L, NumberValue.class.cast(params[0]).get());
        assertEquals(Integer.class, params[1]);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery22(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.IN, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ArrayValue);
        List<?> values = Stream.of(ArrayValue.class.cast(value).get()).map(Value::get).collect(toList());
        assertThat(values, contains("Ada", "Apollo"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name like \"Ada\""})
    public void shouldReturnParserQuery23(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.LIKE, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name not like \"Ada\""})
    public void shouldReturnParserQuery24(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.NOT, condition.getOperator());
        assertEquals("_NOT", condition.getName());
        assertTrue(value instanceof ConditionValue);
        List<Condition> conditions = ConditionValue.class.cast(value).get();
        assertEquals(1, conditions.size());
        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.LIKE, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 and" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery25(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionValue);
        List<Condition> conditions = ConditionValue.class.cast(value).get();
        assertEquals(3, conditions.size());
        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(20L, NumberValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" or age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery26(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.OR, condition.getOperator());
        assertEquals("_OR", condition.getName());
        assertTrue(value instanceof ConditionValue);
        List<Condition> conditions = ConditionValue.class.cast(value).get();
        assertEquals(3, conditions.size());
        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(20L, NumberValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery27(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionValue);
        List<Condition> conditions = ConditionValue.class.cast(value).get();
        assertEquals(3, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(20L, NumberValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.getValue();
        Assertions.assertEquals(Operator.OR, condition.getOperator());

        conditions = ConditionValue.class.cast(condition.getValue()).get();
        assertEquals(1, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} or birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery28(String query) {

        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionValue);
        List<Condition> conditions = ConditionValue.class.cast(value).get();
        assertEquals(3, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(20L, NumberValue.class.cast(value).get());

        condition = conditions.get(2);
        Assertions.assertEquals(Operator.OR, condition.getOperator());

        conditions = ConditionValue.class.cast(condition.getValue()).get();
        assertEquals(2, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));


        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("birthday", condition.getName());
        assertTrue(value instanceof FunctionValue);
        Function function = FunctionValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals("2007-12-03", StringValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery29(String query) {

        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        Value<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionValue);
        List<Condition> conditions = ConditionValue.class.cast(value).get();
        assertEquals(4, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringValue);
        assertEquals("Ada", StringValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberValue);
        assertEquals(20L, NumberValue.class.cast(value).get());

        condition = conditions.get(2);
        Assertions.assertEquals(Operator.OR, condition.getOperator());

        assertEquals(1, ConditionValue.class.cast(condition.getValue()).get().size());

        Condition c = ConditionValue.class.cast(condition.getValue()).get().get(0);
        value = c.getValue();
        Assertions.assertEquals(Operator.EQUALS, c.getOperator());

        assertEquals("siblings", c.getName());
        assertTrue(value instanceof JSONValue);
        JsonObject jsonObject = JSONValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));


        condition = conditions.get(3);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("birthday", condition.getName());
        assertTrue(value instanceof FunctionValue);
        Function function = FunctionValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals("2007-12-03", StringValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
    }



    private SelectQuery checkSelectFromStart(String query) {
        SelectQuery selectQuery = selectQuerySupplier.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        return selectQuery;
    }

}