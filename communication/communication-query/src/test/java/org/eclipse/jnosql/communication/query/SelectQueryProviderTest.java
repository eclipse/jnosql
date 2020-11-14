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

package org.eclipse.jnosql.communication.query;

import jakarta.nosql.Sort;
import jakarta.nosql.SortType;
import jakarta.nosql.query.ArrayQueryValue;
import jakarta.nosql.query.Condition;
import jakarta.nosql.query.ConditionQueryValue;
import jakarta.nosql.query.Function;
import jakarta.nosql.query.FunctionQueryValue;
import jakarta.nosql.query.JSONQueryValue;
import jakarta.nosql.query.NumberQueryValue;
import jakarta.nosql.query.Operator;
import jakarta.nosql.query.ParamQueryValue;
import jakarta.nosql.query.QueryValue;
import jakarta.nosql.query.SelectQuery;
import jakarta.nosql.query.SelectQuery.SelectQueryProvider;
import jakarta.nosql.query.StringQueryValue;
import jakarta.nosql.query.Where;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
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

class SelectQueryProviderTest {

    private SelectQueryProvider selectQueryProvider = new AntlrSelectQueryProvider();

    @Test
    public void shouldReturnErrorWhenStringIsNull() {
        Assertions.assertThrows(NullPointerException.class, () -> selectQueryProvider.apply(null));
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
        SelectQuery selectQuery = selectQueryProvider.apply(query);
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
        SelectQuery selectQuery = selectQueryProvider.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertFalse(selectQuery.getOrderBy().isEmpty());
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getName).collect(toList()), contains("name"));
        MatcherAssert.assertThat(selectQuery.getOrderBy().stream().map(Sort::getType).collect(toList()), 
                Matchers.contains(SortType.ASC));
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God order by name desc"})
    public void shouldReturnParserQuery4(String query) {
        SelectQuery selectQuery = selectQueryProvider.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertFalse(selectQuery.getOrderBy().isEmpty());
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getName).collect(toList()), contains("name"));
        MatcherAssert.assertThat(selectQuery.getOrderBy().stream().map(Sort::getType).collect(toList()),
                Matchers.contains(SortType.DESC));
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God order by name desc age asc"})
    public void shouldReturnParserQuery5(String query) {
        SelectQuery selectQuery = selectQueryProvider.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertFalse(selectQuery.getFields().isEmpty());
        assertThat(selectQuery.getFields(), contains("name", "address"));
        assertFalse(selectQuery.getOrderBy().isEmpty());
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getName).collect(toList()), contains("name", "age"));
        assertThat(selectQuery.getOrderBy().stream().map(Sort::getType).collect(toList()), 
                Matchers.contains(SortType.DESC, SortType.ASC));
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        assertFalse(selectQuery.getWhere().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 12"})
    public void shouldReturnParserQuery6(String query) {
        SelectQuery selectQuery = selectQueryProvider.apply(query);
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
        SelectQuery selectQuery = selectQueryProvider.apply(query);
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
        SelectQuery selectQuery = selectQueryProvider.apply(query);
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
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10L, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina > 10.23"})
    public void shouldReturnParserQuery10(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.GREATER_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina >= 10.23"})
    public void shouldReturnParserQuery11(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.GREATER_EQUALS_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina <= 10.23"})
    public void shouldReturnParserQuery12(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.LESSER_EQUALS_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina < 10.23"})
    public void shouldReturnParserQuery13(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.LESSER_THAN, condition.getOperator());
        assertEquals("stamina", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(10.23, value.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age between 10 and 30"})
    public void shouldReturnParserQuery14(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.BETWEEN, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof ArrayQueryValue);
        ArrayQueryValue arrayValue = ArrayQueryValue.class.cast(value);
        QueryValue<?>[] values = arrayValue.get();
        assertThat(Stream.of(values).map(QueryValue::get).collect(toList()), contains(10L, 30L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"diana\""})
    public void shouldReturnParserQuery15(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("diana", value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = 'diana'"})
    public void shouldReturnParserQuery16(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("diana", value.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = {\"diana\"}"})
    public void shouldReturnParserQuery17(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ArrayQueryValue);
        List<?> values = Stream.of(ArrayQueryValue.class.cast(value).get()).map(QueryValue::get)
                .collect(toList());
        assertThat(values, contains("diana"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = {\"diana\", 17, 20.21}"})
    public void shouldReturnParserQuery18(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ArrayQueryValue);
        List<?> values = Stream.of(ArrayQueryValue.class.cast(value).get())
                .map(QueryValue::get).collect(toList());
        assertThat(values, contains("diana", 17L, 20.21));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery19(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
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
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ParamQueryValue);
        assertEquals("name", ParamQueryValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery21(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals(12L, NumberQueryValue.class.cast(params[0]).get());
        assertEquals(Integer.class, params[1]);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery22(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.IN, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ArrayQueryValue);
        List<?> values = Stream.of(ArrayQueryValue.class.cast(value).get())
                .map(QueryValue::get).collect(toList());
        assertThat(values, contains("Ada", "Apollo"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name like \"Ada\""})
    public void shouldReturnParserQuery23(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.LIKE, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name not like \"Ada\""})
    public void shouldReturnParserQuery24(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.NOT, condition.getOperator());
        assertEquals("_NOT", condition.getName());
        assertTrue(value instanceof ConditionQueryValue);
        List<Condition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(1, conditions.size());
        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.LIKE, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 and" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery25(String query) {
        SelectQuery selectQuery = checkSelectFromStart(query);
        assertTrue(selectQuery.getWhere().isPresent());

        Where where = selectQuery.getWhere().get();
        Condition condition = where.getCondition();
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionQueryValue);
        List<Condition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());
        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
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
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.OR, condition.getOperator());
        assertEquals("_OR", condition.getName());
        assertTrue(value instanceof ConditionQueryValue);
        List<Condition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());
        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
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
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionQueryValue);
        List<Condition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        value = condition.getValue();
        Assertions.assertEquals(Operator.OR, condition.getOperator());

        conditions = ConditionQueryValue.class.cast(condition.getValue()).get();
        assertEquals(1, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
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
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionQueryValue);
        List<Condition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(3, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        Assertions.assertEquals(Operator.OR, condition.getOperator());

        conditions = ConditionQueryValue.class.cast(condition.getValue()).get();
        assertEquals(2, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("siblings", condition.getName());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));


        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("birthday", condition.getName());
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals("2007-12-03", StringQueryValue.class.cast(params[0]).get());
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
        QueryValue<?> value = condition.getValue();
        Assertions.assertEquals(Operator.AND, condition.getOperator());
        assertEquals("_AND", condition.getName());
        assertTrue(value instanceof ConditionQueryValue);
        List<Condition> conditions = ConditionQueryValue.class.cast(value).get();
        assertEquals(4, conditions.size());

        condition = conditions.get(0);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof StringQueryValue);
        assertEquals("Ada", StringQueryValue.class.cast(value).get());

        condition = conditions.get(1);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("age", condition.getName());
        assertTrue(value instanceof NumberQueryValue);
        assertEquals(20L, NumberQueryValue.class.cast(value).get());

        condition = conditions.get(2);
        Assertions.assertEquals(Operator.OR, condition.getOperator());

        assertEquals(1, ConditionQueryValue.class.cast(condition.getValue()).get().size());

        Condition c = ConditionQueryValue.class.cast(condition.getValue()).get().get(0);
        value = c.getValue();
        Assertions.assertEquals(Operator.EQUALS, c.getOperator());

        assertEquals("siblings", c.getName());
        assertTrue(value instanceof JSONQueryValue);
        JsonObject jsonObject = JSONQueryValue.class.cast(value).get();
        assertEquals("Brother", jsonObject.getString("apollo"));
        assertEquals("Father", jsonObject.getString("Zeus"));


        condition = conditions.get(3);
        value = condition.getValue();
        Assertions.assertEquals(Operator.EQUALS, condition.getOperator());

        assertEquals("birthday", condition.getName());
        assertTrue(value instanceof FunctionQueryValue);
        Function function = FunctionQueryValue.class.cast(value).get();
        assertEquals("convert", function.getName());
        Object[] params = function.getParams();
        assertEquals("2007-12-03", StringQueryValue.class.cast(params[0]).get());
        assertEquals(LocalDate.class, params[1]);
    }



    private SelectQuery checkSelectFromStart(String query) {
        SelectQuery selectQuery = selectQueryProvider.apply(query);
        assertEquals("God", selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        return selectQuery;
    }

}