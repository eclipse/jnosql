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
package org.jnosql.diana.key.query;

import jakarta.nosql.QueryException;
import jakarta.nosql.key.BucketManager;
import jakarta.nosql.key.KeyValuePreparedStatement;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RemoveQueryParserTest {


    private RemoveQueryParser parser = new RemoveQueryParser();
    private BucketManager manager = Mockito.mock(BucketManager.class);


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove \"Diana\""})
    public void shouldReturnParserQuery1(String query) {

        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);

        parser.query(query, manager);

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains("Diana"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove 12"})
    public void shouldReturnParserQuery2(String query) {

        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);

        parser.query(query, manager);

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains(12L));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove {\"Ana\" : \"Sister\", \"Maria\" : \"Mother\"}"})
    public void shouldReturnParserQuery3(String query) {

        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);

        parser.query(query, manager);

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains("{\"Ana\":\"Sister\",\"Maria\":\"Mother\"}"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove convert(\"2018-01-10\", java.time.LocalDate)"})
    public void shouldReturnParserQuery4(String query) {
        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);

        parser.query(query, manager);

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());

        MatcherAssert.assertThat(value, Matchers.contains(LocalDate.parse("2018-01-10")));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove @id"})
    public void shouldReturnErrorWhenUseParameterInQuery(String query) {
        assertThrows(QueryException.class, () -> parser.query(query, manager));
    }


    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove @id"})
    public void shouldReturnErrorWhenDontBindParameters(String query) {

        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        assertThrows(QueryException.class, prepare::getResultList);
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove @id"})
    public void shouldExecutePrepareStatement(String query) {

        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        prepare.getResultList();

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());

        MatcherAssert.assertThat(value, Matchers.contains(10));
    }

}