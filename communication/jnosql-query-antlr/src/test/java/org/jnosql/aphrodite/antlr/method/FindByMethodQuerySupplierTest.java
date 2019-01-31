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
package org.jnosql.aphrodite.antlr.method;

import org.jnosql.query.Condition;
import org.jnosql.query.ConditionValue;
import org.jnosql.query.Operator;
import org.jnosql.query.ParamValue;
import org.jnosql.query.SelectQuery;
import org.jnosql.query.Sort;
import org.jnosql.query.Value;
import org.jnosql.query.Where;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FindByMethodQuerySupplierTest {

    private FindByMethodQuerySupplier querySupplier = new FindByMethodQuerySupplier();


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findBy"})
    public void shouldReturnParserQuery(String query) {
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
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
        checkNotCondition(query, Operator.EQUALS, "name");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThan"})
    public void shouldReturnParserQuery4(String query) {

        Operator operator = Operator.GREATER_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotGreaterThan"})
    public void shouldReturnParserQuery5(String query) {
        Operator operator = Operator.GREATER_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThanEqual"})
    public void shouldReturnParserQuery6(String query) {

        Operator operator = Operator.GREATER_EQUALS_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotGreaterThanEqual"})
    public void shouldReturnParserQuery7(String query) {
        Operator operator = Operator.GREATER_EQUALS_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThan"})
    public void shouldReturnParserQuery8(String query) {

        Operator operator = Operator.LESSER_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotLessThan"})
    public void shouldReturnParserQuery9(String query) {
        Operator operator = Operator.LESSER_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLessThanEqual"})
    public void shouldReturnParserQuery10(String query) {

        Operator operator = Operator.LESSER_EQUALS_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotLessThanEqual"})
    public void shouldReturnParserQuer11(String query) {
        Operator operator = Operator.LESSER_EQUALS_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeLike"})
    public void shouldReturnParserQuery12(String query) {

        Operator operator = Operator.LIKE;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotLike"})
    public void shouldReturnParserQuery13(String query) {
        Operator operator = Operator.LIKE;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeIn"})
    public void shouldReturnParserQuery14(String query) {

        Operator operator = Operator.IN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotIn"})
    public void shouldReturnParserQuery15(String query) {
        Operator operator = Operator.IN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeAndName"})
    public void shouldReturnParserQuery16(String query) {

        Operator operator = Operator.EQUALS;
        Operator operator2 = Operator.EQUALS;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.AND;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeOrName"})
    public void shouldReturnParserQuery17(String query) {

        Operator operator = Operator.EQUALS;
        Operator operator2 = Operator.EQUALS;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeOrNameLessThan"})
    public void shouldReturnParserQuery18(String query) {

        Operator operator = Operator.EQUALS;
        Operator operator2 = Operator.LESSER_THAN;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeGreaterThanOrNameIn"})
    public void shouldReturnParserQuery19(String query) {

        Operator operator = Operator.GREATER_THAN;
        Operator operator2 = Operator.IN;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByName"})
    public void shouldReturnParserQuery20(String query) {
        checkOrderBy(query, Sort.SortType.ASC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameAsc"})
    public void shouldReturnParserQuery21(String query) {
        Sort.SortType type = Sort.SortType.ASC;
        checkOrderBy(query, type);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDesc"})
    public void shouldReturnParserQuery22(String query) {
        checkOrderBy(query, Sort.SortType.DESC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDescAgeAsc"})
    public void shouldReturnParserQuery23(String query) {

        Sort.SortType type = Sort.SortType.DESC;
        Sort.SortType type2 = Sort.SortType.ASC;
        checkOrderBy(query, type, type2);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDescAge"})
    public void shouldReturnParserQuery24(String query) {
        checkOrderBy(query, Sort.SortType.DESC, Sort.SortType.ASC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameDescAgeDesc"})
    public void shouldReturnParserQuery25(String query) {
        checkOrderBy(query, Sort.SortType.DESC, Sort.SortType.DESC);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByOrderByNameAscAgeAsc"})
    public void shouldReturnParserQuery26(String query) {
        checkOrderBy(query, Sort.SortType.ASC, Sort.SortType.ASC);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeBetween"})
    public void shouldReturnParserQuery27(String query) {

        Operator operator = Operator.BETWEEN;
        String variable = "age";
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(operator, condition.getOperator());
        Value<?>[] values = MethodArrayValue.class.cast(value).get();
        ParamValue param1 = (ParamValue) values[0];
        ParamValue param2 = (ParamValue) values[1];
        assertFalse(param1.get().equals(param2.get()));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"findByAgeNotBetween"})
    public void shouldReturnParserQuery28(String query) {

        Operator operator = Operator.BETWEEN;
        String variable = "age";
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(Operator.NOT, condition.getOperator());
        Condition notCondition =  MethodConditionValue.class.cast(value).get().get(0);
        assertEquals(Operator.BETWEEN, notCondition.getOperator());

        Value<?>[] values = MethodArrayValue.class.cast(notCondition.getValue()).get();
        ParamValue param1 = (ParamValue) values[0];
        ParamValue param2 = (ParamValue) values[1];
        assertFalse(param1.get().equals(param2.get()));
    }



    private void checkOrderBy(String query, Sort.SortType type, Sort.SortType type2) {
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        List<Sort> sorts = selectQuery.getOrderBy();

        assertEquals(2, sorts.size());
        Sort sort = sorts.get(0);
        assertEquals("name", sort.getName());
        assertEquals(type, sort.getType());

        Sort sort2 = sorts.get(1);
        assertEquals("age", sort2.getName());
        assertEquals(type2, sort2.getType());
    }

    private void checkOrderBy(String query, Sort.SortType type) {
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        List<Sort> sorts = selectQuery.getOrderBy();

        assertEquals(1, sorts.size());
        Sort sort = sorts.get(0);
        assertEquals("name", sort.getName());
        assertEquals(type, sort.getType());
    }


    private void checkAppendCondition(String query, Operator operator, Operator operator2, String variable,
                                      String variable2, Operator operatorAppender) {
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(operatorAppender, condition.getOperator());
        assertTrue(value instanceof ConditionValue);
        Condition condition1 = ConditionValue.class.cast(value).get().get(0);
        Condition condition2 = ConditionValue.class.cast(value).get().get(1);

        assertEquals(operator, condition1.getOperator());
        Value<?> param = condition1.getValue();
        assertEquals(operator, condition1.getOperator());
        assertTrue(ParamValue.class.cast(param).get().contains(variable));

        assertEquals(operator2, condition2.getOperator());
        Value<?> param2 = condition2.getValue();
        assertEquals(condition2.getOperator(), operator2);
        assertTrue(ParamValue.class.cast(param2).get().contains(variable2));
    }


    private void checkNotCondition(String query, Operator operator, String variable) {
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(Operator.NOT, condition.getOperator());


        assertEquals("_NOT", condition.getName());
        assertTrue(value instanceof ConditionValue);
        Condition condition1 = ConditionValue.class.cast(value).get().get(0);
        Value<?> param = condition1.getValue();
        assertEquals(operator, condition1.getOperator());
        assertTrue(ParamValue.class.cast(param).get().contains(variable));
    }

    private void checkEqualsQuery(String query, String entity) {
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(Operator.EQUALS, condition.getOperator());
        assertEquals("name", condition.getName());
        assertTrue(value instanceof ParamValue);
        assertTrue(ParamValue.class.cast(value).get().contains("name"));
    }

    private void checkCondition(String query, Operator operator, String variable) {
        String entity = "entity";
        SelectQuery selectQuery = querySupplier.apply(query, entity);
        assertNotNull(selectQuery);
        assertEquals(entity, selectQuery.getEntity());
        assertTrue(selectQuery.getFields().isEmpty());
        assertTrue(selectQuery.getOrderBy().isEmpty());
        assertEquals(0, selectQuery.getLimit());
        assertEquals(0, selectQuery.getSkip());
        Optional<Where> where = selectQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(operator, condition.getOperator());
        assertTrue(ParamValue.class.cast(value).get().contains(variable));
    }
}