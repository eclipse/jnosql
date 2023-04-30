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
import org.eclipse.jnosql.communication.Value;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetQueryParserTest {

    private final GetQueryParser parser = new GetQueryParser();

    @Mock
    private BucketManager manager;

    @Captor
    private ArgumentCaptor<Object> captor;

    @ParameterizedTest(name = "Should be able to parse query: '{0}'")
    @MethodSource("queryData")
    void shouldReturnParserQuery(String query, Object expected) {
        final Stream<Value> stream = parser.query(query, manager);

        verify(manager, never()).get(any(Object.class));
        stream.collect(toList());

        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertThat(value).contains(expected);
    }

    @Test
    @DisplayName("Should throw QueryException when use parameter in query")
    void shouldReturnErrorWhenUseParameterInQuery() {
        assertThatThrownBy(() -> parser.query("get @id", manager))
                .isInstanceOf(QueryException.class)
                .hasMessage("To run a query with a parameter use a PrepareStatement instead.");
    }

    @Test
    @DisplayName("Should throw QueryException when parameter is not bind")
    void shouldReturnErrorWhenCannotBindParameters() {
        KeyValuePreparedStatement prepare = parser.prepare("get @id", manager);

        assertThatThrownBy(prepare::result)
                .isInstanceOf(QueryException.class)
                .hasMessage("Check all the parameters before execute the query, params left: [id]");
    }

    @Test
    @DisplayName("Should be able to execute the PreparedStatement using one parameter")
    void shouldExecutePrepareStatement() {
        KeyValuePreparedStatement prepare = parser.prepare("get @id", manager);
        prepare.bind("id", 10);
        prepare.result().collect(toList());

        verify(manager).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertThat(value).hasSize(1).contains(10);
    }

    @Test
    @DisplayName("Should be able to execute the PreparedStatement using two parameter")
    void shouldExecutePrepareStatement2() {
        KeyValuePreparedStatement prepare = parser.prepare("get @id, @id2", manager);
        prepare.bind("id", 10);
        prepare.bind("id2", 11);

        final Stream<Value> stream = prepare.result();
        stream.collect(toList());

        verify(manager, times(2)).get(captor.capture());
        List<Object> value = captor.getAllValues();

        assertThat(value).hasSize(2).contains(10, 11);
    }

    static Stream<Arguments> queryData() {
        String query1 = """
                get "Diana"
                """;

        String query2 = "get 12";

        String query3 = """
                get { "Ana": "Sister", "Maria": "Mother" }
                """;

        String query4 = """
                get convert("2018-01-10", java.time.LocalDate)
                """;

        String resultOfQuery3 = """
                {"Ana":"Sister","Maria":"Mother"}
                """;
        return Stream.of(
                Arguments.of(query1, "Diana"),
                Arguments.of(query2, 12L),
                Arguments.of(query3, resultOfQuery3.trim()),
                Arguments.of(query4, LocalDate.parse("2018-01-10"))
        );
    }
}
