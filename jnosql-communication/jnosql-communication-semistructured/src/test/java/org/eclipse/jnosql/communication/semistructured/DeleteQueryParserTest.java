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
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.semistructured.CriteriaCondition.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteQueryParserTest {

    private final DeleteQueryParser parser = new DeleteQueryParser();

    private final DatabaseManager manager = Mockito.mock(DatabaseManager.class);

    private final CommunicationObserverParser observer = new CommunicationObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God"})
    void shouldReturnParserQuery(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.columns().isEmpty());
        assertEquals("God", columnQuery.name());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete name, address from God"})
    void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        assertThat(columnQuery.columns()).contains("name", "address");
        assertEquals("God", columnQuery.name());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina > 10.23"})
    void shouldReturnParserQuery11(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals(Element.of("stamina", 10.23), condition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where stamina >= -10.23"})
    void shouldReturnParserQuery12(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals(Element.of("stamina", -10.23), condition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where stamina <= -10.23"})
    void shouldReturnParserQuery13(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals(Element.of("stamina", -10.23), condition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where stamina < -10.23"})
    void shouldReturnParserQuery14(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals(Element.of("stamina", -10.23), condition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where age between 10 and 30"})
    void shouldReturnParserQuery15(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals(Element.of("age", Arrays.asList(10L, 30L)), condition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"diana\""})
    void shouldReturnParserQuery16(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(Element.of("name", "diana"), condition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    void shouldReturnParserQuery18(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        Element element = condition.element();
        List<Element> elements = element.get(new TypeReference<>() {
        });
        Assertions.assertThat(elements).contains(Element.of("apollo", "Brother"),
                Element.of("Zeus", "Father"));
        assertEquals("siblings", element.name());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where age = convert(12, java.lang.Integer)"})
    void shouldReturnParserQuery19(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", element.name());
        assertEquals(Value.of(12), element.value());


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name in (\"Ada\", \"Apollo\")"})
    void shouldReturnParserQuery20(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.IN, condition.condition());
        assertEquals("name", element.name());
        List<String> values = element.get(new TypeReference<>() {
        });
        assertThat(values).contains("Ada", "Apollo");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name like \"Ada\""})
    void shouldReturnParserQuery21(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", element.name());
        assertEquals("Ada", element.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where name not like \"Ada\""})
    void shouldReturnParserQuery22(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.NOT, condition.condition());
        List<CriteriaCondition> conditions = element.get(new TypeReference<>() {
        });
        CriteriaCondition criteriaCondition = conditions.get(0);
        assertEquals(Condition.LIKE, criteriaCondition.condition());
        assertEquals(Element.of("name", "Ada"), criteriaCondition.element());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" and age = 20"})
    void shouldReturnParserQuery23(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.AND, condition.condition());
        List<CriteriaCondition> conditions = element.get(new TypeReference<>() {
        });
        Assertions.assertThat(conditions).contains(eq(Element.of("name", "Ada")),
                eq(Element.of("age", 20L)));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" or age = 20"})
    void shouldReturnParserQuery24(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.OR, condition.condition());
        List<CriteriaCondition> conditions = element.get(new TypeReference<>() {
        });
        Assertions.assertThat(conditions).contains(eq(Element.of("name", "Ada")),
                eq(Element.of("age", 20L)));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    void shouldReturnParserQuery25(String query) {

        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.AND, condition.condition());
        List<CriteriaCondition> conditions = element.get(new TypeReference<>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).condition());
        assertEquals(Condition.EQUALS, conditions.get(1).condition());
        assertEquals(Condition.OR, conditions.get(2).condition());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    void shouldReturnParserQuery26(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery);
        assertTrue(columnQuery.condition().isPresent());
        CriteriaCondition condition = columnQuery.condition().get();
        Element element = condition.element();
        assertEquals(Condition.AND, condition.condition());
        List<CriteriaCondition> conditions = element.get(new TypeReference<>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).condition());
        assertEquals(Condition.EQUALS, conditions.get(1).condition());
        assertEquals(Condition.OR, conditions.get(2).condition());
        assertEquals(Condition.EQUALS, conditions.get(3).condition());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete  from God where age = @age"})
    void shouldReturnErrorWhenNeedPrepareStatement(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, manager, observer));


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    void shouldReturnErrorWhenIsQueryWithParam(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, manager, observer));

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    void shouldReturnErrorWhenDontBindParameters(String query) {

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, observer);
        assertThrows(QueryException.class, prepare::result);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"delete from God where age = @age"})
    void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<DeleteQuery> captor = ArgumentCaptor.forClass(DeleteQuery.class);

        CommunicationPreparedStatement prepare = parser.prepare(query, manager, observer);
        prepare.bind("age", 12);
        prepare.result();
        Mockito.verify(manager).delete(captor.capture());
        DeleteQuery columnQuery = captor.getValue();
        CriteriaCondition criteriaCondition = columnQuery.condition().get();
        Element element = criteriaCondition.element();
        assertEquals(Condition.EQUALS, criteriaCondition.condition());
        assertEquals("age", element.name());
        assertEquals(12, element.get());
    }

    private void checkBaseQuery(DeleteQuery columnQuery) {
        assertTrue(columnQuery.columns().isEmpty());
        assertEquals("God", columnQuery.name());
    }
}