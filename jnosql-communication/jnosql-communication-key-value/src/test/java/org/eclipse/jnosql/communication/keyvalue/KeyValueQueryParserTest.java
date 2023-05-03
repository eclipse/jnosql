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
 *   Elias Nogueira
 *
 */
package org.eclipse.jnosql.communication.keyvalue;

import jakarta.data.exceptions.NonUniqueResultException;
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyValueQueryParserTest {

    private final KeyValueQueryParser parser = new KeyValueQueryParser();

    @Mock
    private BucketManager manager;

    @Captor
    private ArgumentCaptor<List<Object>> captor;

    @Captor
    private ArgumentCaptor<KeyValueEntity> captorValueEntity;

    @Captor
    private ArgumentCaptor<Duration> captorDuration;

    @Captor
    private ArgumentCaptor<Object> captorObject;

    @Test
    @DisplayName("Should parse query using 'get'")
    void shouldReturnParserQuery1() {
        String query = """
                get "Diana"
                """;

        parser.query(query, manager).toList();

        verify(manager).get(captorObject.capture());
        List<Object> value = captorObject.getAllValues();

        assertThat(value).containsExactly("Diana");
    }

    @Test
    @DisplayName("Should parse query using 'put'")
    void shouldReturnParserQuery2() {
        String query = """
                put { "Diana", "Hunt" }
                """;
        parser.query(query, manager);

        verify(manager).put(captorValueEntity.capture());
        KeyValueEntity entity = captorValueEntity.getValue();

        assertSoftly(softly -> {
            softly.assertThat(entity.key()).as("key is equal").isEqualTo("Diana");
            softly.assertThat(entity.value()).as("entity is equal").isEqualTo("Hunt");
        });
    }

    @Test
    @DisplayName("Should parse query using 'del'")
    void shouldReturnParserQuery3() {
        String query = """
                del "Diana"
                """;
        parser.query(query, manager);

        verify(manager).delete(captor.capture());
        List<Object> value = captor.getValue();

        assertThat(value).hasSize(1).contains("Diana");
    }

    @Test
    @DisplayName("Should execute prepared statement using 'del'")
    void shouldExecutePrepareStatement() {
        KeyValuePreparedStatement prepare = parser.prepare("del @id", manager);
        prepare.bind("id", 10);
        prepare.result();

        verify(manager).delete(captor.capture());
        List<Object> value = captor.getValue();

        assertThat(value).hasSize(1).contains(10);
    }

    @Test
    @DisplayName("Should execute prepared statement using 'put'")
    void shouldExecutePrepareStatement1() {
        String query = """
                put { "Diana", @value }
                """;
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("value", "Hunt");
        prepare.result();

        verify(manager).put(captorValueEntity.capture());
        KeyValueEntity entity = captorValueEntity.getValue();

        assertSoftly(softly -> {
            softly.assertThat(entity.key()).as("key is equal").isEqualTo("Diana");
            softly.assertThat(entity.value()).as("entity is equal").isEqualTo("Hunt");
        });
    }

    @Test
    @DisplayName("Should execute prepared statement using 'put' setting a value")
    void shouldExecutePrepareStatement2() {
        String query = """
                put { "Diana", @value, 10 second }
                """;
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("value", "Hunt");
        prepare.result();

        verify(manager).put(captorValueEntity.capture(), captorDuration.capture());
        KeyValueEntity entity = captorValueEntity.getValue();
        final Duration duration = captorDuration.getValue();

        assertSoftly(softly -> {
            softly.assertThat(entity.key()).as("key is equal").isEqualTo("Diana");
            softly.assertThat(entity.value()).as("entity is equal").isEqualTo("Hunt");
            softly.assertThat(duration).as("duration is equal").isEqualTo(Duration.ofSeconds(10L));
        });
    }

    @Test
    @DisplayName("Should return a single result")
    void shouldReturnSingleResult() {
        String query = "get @id";

        when(manager.get(10)).thenReturn(Optional.of(Value.of(10)));

        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("id", 10);
        final Optional<Value> result = prepare.singleResult();

        verify(manager).get(captorObject.capture());
        List<Object> value = captorObject.getAllValues();

        assertSoftly(softly -> {
            softly.assertThat(value).as("key is equal").hasSize(1).contains(10);
            softly.assertThat(result.get().get()).as("").isEqualTo(10);
        });
    }

    @Test
    @DisplayName("Should return an empty single result")
    void shouldReturnEmptySingleResult() {
        when(manager.get(10)).thenReturn(Optional.empty());

        KeyValuePreparedStatement prepare = parser.prepare("get @id", manager);
        prepare.bind("id", 10);
        final Optional<Value> result = prepare.singleResult();

        verify(manager).get(captorObject.capture());
        List<Object> value = captorObject.getAllValues();

        assertSoftly(softly -> {
            softly.assertThat(value).as("key is equal").hasSize(1).contains(10);
            softly.assertThat(result).as("result is present").isNotPresent();
        });
    }

    @Test
    @DisplayName("Should throw NonUniqueResultException when get single result")
    void shouldReturnErrorSingleResult() {
        when(manager.get(10)).thenReturn(Optional.of(Value.of(10)));
        when(manager.get(11)).thenReturn(Optional.of(Value.of(11)));

        KeyValuePreparedStatement prepare = parser.prepare("get @id, @id2", manager);
        prepare.bind("id", 10);
        prepare.bind("id2", 11);

        assertThatThrownBy(prepare::singleResult).isInstanceOf(NonUniqueResultException.class)
                .hasMessage("The select returns more than one entity, select: get @id, @id2");
    }
}