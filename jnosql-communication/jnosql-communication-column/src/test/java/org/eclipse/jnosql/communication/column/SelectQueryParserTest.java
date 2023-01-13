/*
 *
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.eclipse.jnosql.communication.column;

import org.assertj.core.api.Assertions;
import org.eclipse.jnosql.communication.Condition;
import org.eclipse.jnosql.communication.QueryException;
import org.eclipse.jnosql.communication.Sort;
import org.eclipse.jnosql.communication.SortType;
import org.eclipse.jnosql.communication.TypeReference;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.eclipse.jnosql.communication.column.ColumnCondition.eq;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectQueryParserTest {

    private final SelectQueryParser parser = new SelectQueryParser();

    private final ColumnManager manager = Mockito.mock(ColumnManager.class);

    private final ColumnObserverParser observer = new ColumnObserverParser() {
    };


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select name, address from God"})
    public void shouldReturnParserQuery1(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertThat(columnQuery.columns()).contains("name", "address");
        assertTrue(columnQuery.sorts().isEmpty());
        assertEquals(0L, columnQuery.limit());
        assertEquals(0L, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name"})
    public void shouldReturnParserQuery3(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.columns().isEmpty());
        assertThat(columnQuery.sorts()).contains(Sort.of("name", SortType.ASC));
        assertEquals(0L, columnQuery.limit());
        assertEquals(0L, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name asc"})
    public void shouldReturnParserQuery4(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.columns().isEmpty());
        assertThat(columnQuery.sorts()).contains(Sort.of("name", SortType.ASC));
        assertEquals(0L, columnQuery.limit());
        assertEquals(0L, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc"})
    public void shouldReturnParserQuery5(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.columns().isEmpty());
        assertThat(columnQuery.sorts()).contains(Sort.of("name", SortType.DESC));
        assertEquals(0L, columnQuery.limit());
        assertEquals(0L, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God order by name desc age asc"})
    public void shouldReturnParserQuery6(String query) {

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.columns().isEmpty());
        assertThat(columnQuery.sorts()).contains(Sort.of("name", SortType.DESC), Sort.of("age",
                SortType.ASC));
        assertEquals(0L, columnQuery.limit());
        assertEquals(0L, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 12"})
    public void shouldReturnParserQuery7(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 12L);
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God limit 12"})
    public void shouldReturnParserQuery8(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 12L, 0L);
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God skip 10 limit 12"})
    public void shouldReturnParserQuery9(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        assertTrue(columnQuery.columns().isEmpty());
        assertTrue(columnQuery.sorts().isEmpty());
        assertEquals(12L, columnQuery.limit());
        assertEquals(10L, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
        assertFalse(columnQuery.condition().isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = 10"})
    public void shouldReturnParserQuery10(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(Column.of("age", 10L), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina > 10.23"})
    public void shouldReturnParserQuery11(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.GREATER_THAN, condition.condition());
        assertEquals(Column.of("stamina", 10.23), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina >= -10.23"})
    public void shouldReturnParserQuery12(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.GREATER_EQUALS_THAN, condition.condition());
        assertEquals(Column.of("stamina", -10.23), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina <= -10.23"})
    public void shouldReturnParserQuery13(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.LESSER_EQUALS_THAN, condition.condition());
        assertEquals(Column.of("stamina", -10.23), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where stamina < -10.23"})
    public void shouldReturnParserQuery14(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.LESSER_THAN, condition.condition());
        assertEquals(Column.of("stamina", -10.23), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age between 10 and 30"})
    public void shouldReturnParserQuery15(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.BETWEEN, condition.condition());
        assertEquals(Column.of("age", Arrays.asList(10L, 30L)), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"diana\""})
    public void shouldReturnParserQuery16(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals(Column.of("name", "diana"), condition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery18(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();

        assertEquals(Condition.EQUALS, condition.condition());
        Column column = condition.column();
        List<Column> columns = column.get(new TypeReference<>() {
        });
        Assertions.assertThat(columns).contains(Column.of("apollo", "Brother"),
                Column.of("Zeus", "Father"));
        assertEquals("siblings", column.name());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = convert(12, java.lang.Integer)"})
    public void shouldReturnParserQuery19(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.EQUALS, condition.condition());
        assertEquals("age", column.name());
        assertEquals(Value.of(12), column.value());


    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name in (\"Ada\", \"Apollo\")"})
    public void shouldReturnParserQuery20(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.IN, condition.condition());
        assertEquals("name", column.name());
        List<String> values = column.get(new TypeReference<>() {
        });
        assertThat(values).contains("Ada", "Apollo");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name like \"Ada\""})
    public void shouldReturnParserQuery21(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.LIKE, condition.condition());
        assertEquals("name", column.name());
        assertEquals("Ada", column.get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select * from God where name not like \"Ada\""})
    public void shouldReturnParserQuery22(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.NOT, condition.condition());
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        ColumnCondition columnCondition = conditions.get(0);
        assertEquals(Condition.LIKE, columnCondition.condition());
        assertEquals(Column.of("name", "Ada"), columnCondition.column());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20"})
    public void shouldReturnParserQuery23(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.AND, condition.condition());
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        Assertions.assertThat(conditions).contains(eq(Column.of("name", "Ada")),
                eq(Column.of("age", 20L)));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" or age = 20"})
    public void shouldReturnParserQuery24(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.OR, condition.condition());
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        Assertions.assertThat(conditions).contains(eq(Column.of("name", "Ada")),
                eq(Column.of("age", 20L)));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"}"})
    public void shouldReturnParserQuery25(String query) {

        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.AND, condition.condition());
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).condition());
        assertEquals(Condition.EQUALS, conditions.get(1).condition());
        assertEquals(Condition.OR, conditions.get(2).condition());

    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where name = \"Ada\" and age = 20 or" +
            " siblings = {\"apollo\": \"Brother\", \"Zeus\": \"Father\"} and birthday =" +
            " convert(\"2007-12-03\", java.time.LocalDate)"})
    public void shouldReturnParserQuery26(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);
        parser.query(query, manager, observer);
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();

        checkBaseQuery(columnQuery, 0L, 0L);
        assertTrue(columnQuery.condition().isPresent());
        ColumnCondition condition = columnQuery.condition().get();
        Column column = condition.column();
        assertEquals(Condition.AND, condition.condition());
        List<ColumnCondition> conditions = column.get(new TypeReference<>() {
        });
        assertEquals(Condition.EQUALS, conditions.get(0).condition());
        assertEquals(Condition.EQUALS, conditions.get(1).condition());
        assertEquals(Condition.OR, conditions.get(2).condition());
        assertEquals(Condition.EQUALS, conditions.get(3).condition());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnErrorWhenIsQueryWithParam(String query) {

        assertThrows(QueryException.class, () -> parser.query(query, manager, observer));


    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldReturnErrorWhenDontBindParameters(String query) {

        ColumnPreparedStatement prepare = parser.prepare(query, manager, observer);
        assertThrows(QueryException.class, prepare::result);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"select  * from God where age = @age"})
    public void shouldExecutePrepareStatement(String query) {
        ArgumentCaptor<ColumnQuery> captor = ArgumentCaptor.forClass(ColumnQuery.class);

        ColumnPreparedStatement prepare = parser.prepare(query, manager, observer);
        prepare.bind("age", 12);
        prepare.result();
        Mockito.verify(manager).select(captor.capture());
        ColumnQuery columnQuery = captor.getValue();
        ColumnCondition columnCondition = columnQuery.condition().get();
        Column column = columnCondition.column();
        assertEquals(Condition.EQUALS, columnCondition.condition());
        assertEquals("age", column.name());
        assertEquals(12, column.get());
    }

    private void checkBaseQuery(ColumnQuery columnQuery, long limit, long skip) {
        assertTrue(columnQuery.columns().isEmpty());
        assertTrue(columnQuery.sorts().isEmpty());
        assertEquals(limit, columnQuery.limit());
        assertEquals(skip, columnQuery.skip());
        assertEquals("God", columnQuery.columnFamily());
    }
}
