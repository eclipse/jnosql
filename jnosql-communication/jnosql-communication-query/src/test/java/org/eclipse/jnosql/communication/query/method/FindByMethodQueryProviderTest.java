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
package org.eclipse.jnosql.communication.query.method;

import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.Sort;
import org.eclipse.jnosql.communication.SortType;
import org.eclipse.jnosql.communication.query.ConditionQueryValue;
import org.eclipse.jnosql.communication.query.ParamQueryValue;
import org.eclipse.jnosql.communication.query.QueryCondition;
import org.eclipse.jnosql.communication.query.QueryValue;
import org.eclipse.jnosql.communication.query.SelectQuery;
import org.eclipse.jnosql.communication.query.Where;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindByMethodQueryProviderTest {

    private final FindByMethodQueryProvider queryProvider = new FindByMethodQueryProvider();


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBy"})
    public void shouldReturnParserQuery(String query) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertFalse(where.isPresent());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByName"})
    public void shouldReturnParserQuery1(String query) {
        String entity = "entity";
        checkEqualsQuery(query, entity);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameEquals"})
    public void shouldReturnParserQuery2(String query) {
        String entity = "entity";
        checkEqualsQuery(query, entity);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByNameNotEquals"})
    public void shouldReturnParserQuery3(String query) {
        checkNotCondition(query, Condition.EQUALS, "name");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThan"})
    public void shouldReturnParserQuery4(String query) {

        Condition operator = Condition.GREATER_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotGreaterThan"})
    public void shouldReturnParserQuery5(String query) {
        Condition operator = Condition.GREATER_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThanEqual"})
    public void shouldReturnParserQuery6(String query) {

        Condition operator = Condition.GREATER_EQUALS_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotGreaterThanEqual"})
    public void shouldReturnParserQuery7(String query) {
        Condition operator = Condition.GREATER_EQUALS_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThan"})
    public void shouldReturnParserQuery8(String query) {

        Condition operator = Condition.LESSER_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotLessThan"})
    public void shouldReturnParserQuery9(String query) {
        Condition operator = Condition.LESSER_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThanEqual"})
    public void shouldReturnParserQuery10(String query) {

        Condition operator = Condition.LESSER_EQUALS_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotLessThanEqual"})
    public void shouldReturnParserQuery11(String query) {
        Condition operator = Condition.LESSER_EQUALS_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLike"})
    public void shouldReturnParserQuery12(String query) {

        Condition operator = Condition.LIKE;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotLike"})
    public void shouldReturnParserQuery13(String query) {
        Condition operator = Condition.LIKE;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeIn"})
    public void shouldReturnParserQuery14(String query) {

        Condition operator = Condition.IN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotIn"})
    public void shouldReturnParserQuery15(String query) {
        Condition operator = Condition.IN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeAndName"})
    public void shouldReturnParserQuery16(String query) {

        Condition operator = Condition.EQUALS;
        Condition operator2 = Condition.EQUALS;
        String variable = "age";
        String variable2 = "name";
        Condition operatorAppender = Condition.AND;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeOrName"})
    public void shouldReturnParserQuery17(String query) {

        Condition operator = Condition.EQUALS;
        Condition operator2 = Condition.EQUALS;
        String variable = "age";
        String variable2 = "name";
        Condition operatorAppender = Condition.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeOrNameLessThan"})
    public void shouldReturnParserQuery18(String query) {

        Condition operator = Condition.EQUALS;
        Condition operator2 = Condition.LESSER_THAN;
        String variable = "age";
        String variable2 = "name";
        Condition operatorAppender = Condition.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThanOrNameIn"})
    public void shouldReturnParserQuery19(String query) {

        Condition operator = Condition.GREATER_THAN;
        Condition operator2 = Condition.IN;
        String variable = "age";
        String variable2 = "name";
        Condition operatorAppender = Condition.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByName"})
    public void shouldReturnParserQuery20(String query) {
        checkOrderBy(query, SortType.ASC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameAsc"})
    public void shouldReturnParserQuery21(String query) {
        SortType type = SortType.ASC;
        checkOrderBy(query, type);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDesc"})
    public void shouldReturnParserQuery22(String query) {
        checkOrderBy(query, SortType.DESC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDescAgeAsc"})
    public void shouldReturnParserQuery23(String query) {

        SortType type = SortType.DESC;
        SortType type2 = SortType.ASC;
        checkOrderBy(query, type, type2);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDescAge"})
    public void shouldReturnParserQuery24(String query) {
        checkOrderBy(query, SortType.DESC, SortType.ASC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDescAgeDesc"})
    public void shouldReturnParserQuery25(String query) {
        checkOrderBy(query, SortType.DESC, SortType.DESC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameAscAgeAsc"})
    public void shouldReturnParserQuery26(String query) {
        checkOrderBy(query, SortType.ASC, SortType.ASC);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeBetween"})
    public void shouldReturnParserQuery27(String query) {

        Condition operator = Condition.BETWEEN;
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        QueryValue<?> value = condition.value();
        assertEquals(operator, condition.condition());
        QueryValue<?>[] values = MethodArrayValue.class.cast(value).get();
        ParamQueryValue param1 = (ParamQueryValue) values[0];
        ParamQueryValue param2 = (ParamQueryValue) values[1];
        assertNotEquals(param2.get(), param1.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotBetween"})
    public void shouldReturnParserQuery28(String query) {

        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        QueryValue<?> value = condition.value();
        assertEquals(Condition.NOT, condition.condition());
        QueryCondition notCondition =  MethodConditionValue.class.cast(value).get().get(0);
        assertEquals(Condition.BETWEEN, notCondition.condition());

        QueryValue<?>[] values = MethodArrayValue.class.cast(notCondition.value()).get();
        ParamQueryValue param1 = (ParamQueryValue) values[0];
        ParamQueryValue param2 = (ParamQueryValue) values[1];
        assertNotEquals(param2.get(), param1.get());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_Currency"})
    public void shouldRunQuery29(String query) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("salary.currency", condition.name());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyAndCredential_Role"})
    public void shouldRunQuery30(String query) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        Assertions.assertEquals(Condition.AND, condition.condition());
        final QueryValue<?> value = condition.value();
        QueryCondition condition1 = ConditionQueryValue.class.cast(value).get().get(0);
        QueryCondition condition2 = ConditionQueryValue.class.cast(value).get().get(1);
        assertEquals("salary.currency", condition1.name());
        assertEquals("credential.role", condition2.name());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyAndName"})
    public void shouldRunQuery31(String query) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        Assertions.assertEquals(Condition.AND, condition.condition());
        final QueryValue<?> value = condition.value();
        QueryCondition condition1 = ConditionQueryValue.class.cast(value).get().get(0);
        QueryCondition condition2 = ConditionQueryValue.class.cast(value).get().get(1);
        assertEquals("salary.currency", condition1.name());
        assertEquals("name", condition2.name());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBySalary_CurrencyOrderBySalary_Value"})
    public void shouldRunQuery32(String query) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertFalse(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        Assertions.assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("salary.currency", condition.name());

        final Sort sort = selectQuery.orderBy().get(0);
        Assertions.assertEquals("salary.value", sort.name());
    }


    private void checkOrderBy(String query, SortType type, SortType type2) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        List<Sort> sorts = selectQuery.orderBy();

        assertEquals(2, sorts.size());
        Sort sort = sorts.get(0);
        assertEquals("name", sort.name());
        assertEquals(type, sort.type());

        Sort sort2 = sorts.get(1);
        assertEquals("age", sort2.name());
        assertEquals(type2, sort2.type());
    }

    private void checkOrderBy(String query, SortType type) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        List<Sort> sorts = selectQuery.orderBy();

        assertEquals(1, sorts.size());
        Sort sort = sorts.get(0);
        assertEquals("name", sort.name());
        assertEquals(type, sort.type());
    }


    private void checkAppendCondition(String query, Condition operator, Condition operator2, String variable,
                                      String variable2, Condition operatorAppender) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        QueryValue<?> value = condition.value();
        assertEquals(operatorAppender, condition.condition());
        assertTrue(value instanceof ConditionQueryValue);
        QueryCondition condition1 = ConditionQueryValue.class.cast(value).get().get(0);
        QueryCondition condition2 = ConditionQueryValue.class.cast(value).get().get(1);

        assertEquals(operator, condition1.condition());
        QueryValue<?> param = condition1.value();
        assertEquals(operator, condition1.condition());
        assertTrue(ParamQueryValue.class.cast(param).get().contains(variable));

        assertEquals(operator2, condition2.condition());
        QueryValue<?> param2 = condition2.value();
        assertEquals(condition2.condition(), operator2);
        assertTrue(ParamQueryValue.class.cast(param2).get().contains(variable2));
    }


    private void checkNotCondition(String query, Condition operator, String variable) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        QueryValue<?> value = condition.value();
        assertEquals(Condition.NOT, condition.condition());


        assertEquals("_NOT", condition.name());
        assertTrue(value instanceof ConditionQueryValue);
        QueryCondition condition1 = ConditionQueryValue.class.cast(value).get().get(0);
        QueryValue<?> param = condition1.value();
        assertEquals(operator, condition1.condition());
        assertTrue(ParamQueryValue.class.cast(param).get().contains(variable));
    }

    private void checkEqualsQuery(String query, String entity) {
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        QueryValue<?> value = condition.value();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("name", condition.name());
        assertTrue(value instanceof ParamQueryValue);
        assertTrue(ParamQueryValue.class.cast(value).get().contains("name"));
    }

    private void checkCondition(String query, Condition operator, String variable) {
        String entity = "entity";
        SelectQuery selectQuery = queryProvider.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.entity());
        assertTrue(selectQuery.fields().isEmpty());
        assertTrue(selectQuery.orderBy().isEmpty());
        assertEquals(0, selectQuery.limit());
        assertEquals(0, selectQuery.skip());
        Optional<Where> where = selectQuery.where();
        assertTrue(where.isPresent());
        QueryCondition condition = where.get().condition();
        QueryValue<?> value = condition.value();
        assertEquals(operator, condition.condition());
        assertTrue(ParamQueryValue.class.cast(value).get().contains(variable));
    }
}