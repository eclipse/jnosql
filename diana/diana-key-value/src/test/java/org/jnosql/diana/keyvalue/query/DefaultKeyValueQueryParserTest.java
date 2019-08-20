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
package org.jnosql.diana.keyvalue.query;

import jakarta.nosql.NonUniqueResultException;
import jakarta.nosql.Value;
import jakarta.nosql.keyvalue.BucketManager;
import jakarta.nosql.keyvalue.KeyValueEntity;
import jakarta.nosql.keyvalue.KeyValuePreparedStatement;
import jakarta.nosql.keyvalue.KeyValueQueryParser;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultKeyValueQueryParserTest {

    private KeyValueQueryParser parser = new DefaultKeyValueQueryParser();
    private BucketManager manager = Mockito.mock(BucketManager.class);



    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get \"Diana\""})
    public void shouldReturnParserQuery1(String query) {

        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);

        parser.query(query, manager).collect(Collectors.toList());

        Mockito.verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains("Diana"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {\"Diana\", \"Hunt\"}"})
    public void shouldReturnParserQuery2(String query) {

        ArgumentCaptor<KeyValueEntity> captor = ArgumentCaptor.forClass(KeyValueEntity.class);

        parser.query(query, manager);

        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();

        assertEquals("Diana", entity.getKey());
        assertEquals("Hunt", entity.getValue());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove \"Diana\""})
    public void shouldReturnParserQuery3(String query) {

        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);

        parser.query(query, manager);

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains("Diana"));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"remove @id"})
    public void shouldExecutePrepareStatement(String query) {

        ArgumentCaptor<List<Object>> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        prepare.getResult();

        Mockito.verify(manager).remove(captor.capture());
        List<Object> value = captor.getValue();

        assertEquals(1, value.size());

        MatcherAssert.assertThat(value, Matchers.contains(10));
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"put {\"Diana\", @value}"})
    public void shouldExecutePrepareStatement1(String query) {
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("value", "Hunt");
        prepare.getResult();
        ArgumentCaptor<KeyValueEntity> captor = ArgumentCaptor.forClass(KeyValueEntity.class);

        Mockito.verify(manager).put(captor.capture());
        KeyValueEntity entity = captor.getValue();

        assertEquals("Diana", entity.getKey());
        assertEquals("Hunt", entity.getValue());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id"})
    public void shouldReturnSingleResult(String query) {

        Mockito.when(manager.get(10)).thenReturn(Optional.of(Value.of(10L)));
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        final Optional<Value> result = prepare.getSingleResult();

        Mockito.verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains(10));
        assertEquals(10L, result.get().get());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id"})
    public void shouldReturnEmptySingleResult(String query) {

        Mockito.when(manager.get(10)).thenReturn(Optional.empty());
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        final Optional<Value> result = prepare.getSingleResult();

        Mockito.verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertEquals(1, value.size());
        MatcherAssert.assertThat(value, Matchers.contains(10));
        assertFalse(result.isPresent());
    }

    @ParameterizedTest(name = "Should parser the query {0}")
    @ValueSource(strings = {"get @id, @id2"})
    public void shouldReturnErrorSingleResult(String query) {

        Mockito.when(manager.get(10)).thenReturn(Optional.of(Value.of(10)));
        Mockito.when(manager.get(11)).thenReturn(Optional.of(Value.of(11)));
        ArgumentCaptor<Object> captor = ArgumentCaptor.forClass(List.class);
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        prepare.bind("id2", 11);
        Assertions.assertThrows(NonUniqueResultException.class, () -> prepare.getSingleResult());
    }
}