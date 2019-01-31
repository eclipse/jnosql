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
import org.jnosql.query.DeleteQuery;
import org.jnosql.query.Operator;
import org.jnosql.query.ParamValue;
import org.jnosql.query.Value;
import org.jnosql.query.Where;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteByMethodQuerySupplierTest {

    private DeleteByMethodQuerySupplier querySupplier = new DeleteByMethodQuerySupplier();


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteBy"})
    public void shouldReturnParserQuery(String query) {
        String entity = "entity";
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
        assertFalse(where.isPresent());
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByName"})
    public void shouldReturnParserQuery1(String query) {
        String entity = "entity";
        checkEqualsQuery(query, entity);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameEquals"})
    public void shouldReturnParserQuery2(String query) {
        String entity = "entity";
        checkEqualsQuery(query, entity);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByNameNotEquals"})
    public void shouldReturnParserQuery3(String query) {
        checkNotCondition(query, Operator.EQUALS, "name");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThan"})
    public void shouldReturnParserQuery4(String query) {

        Operator operator = Operator.GREATER_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeNotGreaterThan"})
    public void shouldReturnParserQuery5(String query) {
        Operator operator = Operator.GREATER_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThanEqual"})
    public void shouldReturnParserQuery6(String query) {

        Operator operator = Operator.GREATER_EQUALS_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeNotGreaterThanEqual"})
    public void shouldReturnParserQuery7(String query) {
        Operator operator = Operator.GREATER_EQUALS_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLessThan"})
    public void shouldReturnParserQuery8(String query) {

        Operator operator = Operator.LESSER_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeNotLessThan"})
    public void shouldReturnParserQuery9(String query) {
        Operator operator = Operator.LESSER_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLessThanEqual"})
    public void shouldReturnParserQuery10(String query) {

        Operator operator = Operator.LESSER_EQUALS_THAN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeNotLessThanEqual"})
    public void shouldReturnParserQuer11(String query) {
        Operator operator = Operator.LESSER_EQUALS_THAN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeLike"})
    public void shouldReturnParserQuery12(String query) {

        Operator operator = Operator.LIKE;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeNotLike"})
    public void shouldReturnParserQuery13(String query) {
        Operator operator = Operator.LIKE;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeIn"})
    public void shouldReturnParserQuery14(String query) {

        Operator operator = Operator.IN;
        String variable = "age";
        checkCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeNotIn"})
    public void shouldReturnParserQuery15(String query) {
        Operator operator = Operator.IN;
        String variable = "age";
        checkNotCondition(query, operator, variable);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeAndName"})
    public void shouldReturnParserQuery16(String query) {

        Operator operator = Operator.EQUALS;
        Operator operator2 = Operator.EQUALS;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.AND;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeOrName"})
    public void shouldReturnParserQuery17(String query) {

        Operator operator = Operator.EQUALS;
        Operator operator2 = Operator.EQUALS;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeOrNameLessThan"})
    public void shouldReturnParserQuery18(String query) {

        Operator operator = Operator.EQUALS;
        Operator operator2 = Operator.LESSER_THAN;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeGreaterThanOrNameIn"})
    public void shouldReturnParserQuery19(String query) {

        Operator operator = Operator.GREATER_THAN;
        Operator operator2 = Operator.IN;
        String variable = "age";
        String variable2 = "name";
        Operator operatorAppender = Operator.OR;
        checkAppendCondition(query, operator, operator2, variable, variable2, operatorAppender);
    }



    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"deleteByAgeBetween"})
    public void shouldReturnParserQuery27(String query) {

        Operator operator = Operator.BETWEEN;
        String variable = "age";
        String entity = "entity";
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
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
    @ValueSource(strings = {"deleteByAgeNotBetween"})
    public void shouldReturnParserQuery28(String query) {

        Operator operator = Operator.BETWEEN;
        String variable = "age";
        String entity = "entity";
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
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


    private void checkAppendCondition(String query, Operator operator, Operator operator2, String variable,
                                      String variable2, Operator operatorAppender) {
        String entity = "entity";
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
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
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
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
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
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
        DeleteQuery deleteQuery = querySupplier.apply(query, entity);
        assertNotNull(deleteQuery);
        assertEquals(entity, deleteQuery.getEntity());
        assertTrue(deleteQuery.getFields().isEmpty());
        Optional<Where> where = deleteQuery.getWhere();
        assertTrue(where.isPresent());
        Condition condition = where.get().getCondition();
        Value<?> value = condition.getValue();
        assertEquals(operator, condition.getOperator());
        assertTrue(ParamValue.class.cast(value).get().contains(variable));
    }
}