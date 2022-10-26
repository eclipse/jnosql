/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
package org.eclipse.jnosql.communication.keyvalue.query;

import jakarta.nosql.QueryException;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValuePreparedStatement;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

class GetQueryParserTest {

    private final GetQueryParser parser = new GetQueryParser();

    private final BucketManager manager = Mockito.mock(BucketManager.class);


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get \"Diana\""})
    public void shouldReturnParserQuery1(String query) {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);

        final Stream<Value> stream = parser.query(query, manager);
        verify(manager, Mockito.never()).get(Mockito.any(Object.class));
        stream.collect(Collectors.toList());
        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());
        assertThat(value).contains("Diana");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get 12"})
    public void shouldReturnParserQuery2(String query) {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);

        final Stream<Value> stream = parser.query(query, manager);

        verify(manager, Mockito.never()).get(Mockito.any(Object.class));
        stream.collect(Collectors.toList());
        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());
        assertThat(value).contains(12L);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get {\"Ana\" : \"Sister\", \"Maria\" : \"Mother\"}"})
    public void shouldReturnParserQuery3(String query) {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);

        final Stream<Value> stream = parser.query(query, manager);

        verify(manager, Mockito.never()).get(Mockito.any(Object.class));
        stream.collect(Collectors.toList());
        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());
        assertThat(value).contains("{\"Ana\":\"Sister\",\"Maria\":\"Mother\"}");
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get convert(\"2018-01-10\", java.time.LocalDate)"})
    public void shouldReturnParserQuery4(String query) {
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);

        final Stream<Value> stream = parser.query(query, manager);

        verify(manager, Mockito.never()).get(Mockito.any(Object.class));
        stream.collect(Collectors.toList());
        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());

        assertThat(value).contains(LocalDate.parse("2018-01-10"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id"})
    public void shouldReturnErrorWhenUseParameterInQuery(String query) {
        assertThrows(QueryException.class, () -> parser.query(query, manager));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id"})
    public void shouldReturnErrorWhenDontBindParameters(String query) {

        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        assertThrows(QueryException.class, prepare::getResult);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id"})
    public void shouldExecutePrepareStatement(String query) {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        prepare.getResult().collect(Collectors.toList());

        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());

        assertThat(value).contains(10);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id, @id2"})
    public void shouldExecutePrepareStatement2(String query) {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        prepare.bind("id2", 11);
        final Stream<Value> stream = prepare.getResult();
        stream.collect(Collectors.toList());
        verify(manager, Mockito.times(2)).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(2, value.size());

        assertThat(value).contains(10, 11);
    }
}