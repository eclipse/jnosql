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

import org.eclipse.jnosql.communication.QueryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.assertArg;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PutQueryParserTest {

    private final PutQueryParser parser = new PutQueryParser();

    @Mock
    private BucketManager manager;

    @Test
    @DisplayName("Should be able to parse query")
    void shouldReturnParserQuery1() {
        String query = """
                put { "Diana", "Hunt" }
                """;
        parser.query(query, manager);

        verify(manager).put(assertArg((KeyValueEntity entity) ->
                assertSoftly(softly -> {
                    softly.assertThat(entity.key()).as("key is expected").isEqualTo("Diana");
                    softly.assertThat(entity.value()).as("value is expected").isEqualTo("Hunt");
                })));
    }

    @Test
    @DisplayName("Should be able to parse query retrieving Duration")
    void shouldReturnParserQuery2() {
        String query = """
                put { "Diana", "goddess of hunt", 10 hour }
                """;
        parser.query(query, manager);

        verify(manager).put(
                assertArg((KeyValueEntity entity) ->
                        assertSoftly(softly -> {
                            softly.assertThat(entity.key()).as("key is expected").isEqualTo("Diana");
                            softly.assertThat(entity.value()).as("value is expected").isEqualTo("goddess of hunt");
                        })),
                assertArg((Duration duration) ->
                        assertThat(duration).isEqualTo(Duration.ofHours(10))));
    }

    @Test
    @DisplayName("Should throw QueryException when use parameter in query")
    void shouldReturnErrorWhenUseParameterInQuery() {
        String query = """
                put { "Diana", @value }
                """;

        assertThatThrownBy(() -> parser.query(query, manager))
                .isInstanceOf(QueryException.class)
                .hasMessage("To run a query with a parameter use a PrepareStatement instead.");
    }

    @Test
    @DisplayName("Should throw QueryException when parameter is not bind")
    void shouldReturnErrorWhenCannotBindParameters() {
        String query = """
                put { "Diana", @value }
                """;
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);

        assertThatThrownBy(prepare::result)
                .isInstanceOf(QueryException.class)
                .hasMessage("Check all the parameters before execute the query, params left: [value]");
    }

    @Test
    @DisplayName("Should be able to execute the PreparedStatement using one parameter")
    void shouldExecutePrepareStatement() {
        String query = """
                put { "Diana", @value }
                """;
        KeyValuePreparedStatement prepare = parser.prepare(query, manager);
        prepare.bind("value", "Hunt");
        prepare.result();

        verify(manager).put(assertArg((KeyValueEntity entity) ->
                assertSoftly(softly -> {
                    softly.assertThat(entity.key()).as("key is expected").isEqualTo("Diana");
                    softly.assertThat(entity.value()).as("value is expected").isEqualTo("Hunt");
                })));
    }
}
