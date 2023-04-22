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
package org.eclipse.jnosql.communication.writer;

import org.eclipse.jnosql.communication.ValueWriter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TemporalWriterTest {

    private final ValueWriter<Temporal, String> valueWriter = new TemporalValueWriter();

    @ParameterizedTest(name = "must be compatible to {0}")
    @DisplayName("Should check for compatibility")
    @ValueSource(classes = {Temporal.class, LocalDate.class, LocalDateTime.class, LocalTime.class, Year.class, YearMonth.class, ZonedDateTime.class})
    void shouldVerifyCompatibility(Class<?> supportedClass) {
        assertThat(valueWriter.test(supportedClass)).isTrue();
    }

    @Test
    @DisplayName("Should check for incompatibility")
    void shouldBeNotCompatible() {
        assertThat(valueWriter.test(Boolean.class)).isFalse();
    }

    @ParameterizedTest(name = "must convert {0}")
    @DisplayName("Should be able to convert temporal")
    @MethodSource("temporalDataForConversion")
    void shouldConvert(Temporal temporal) {
        String result = valueWriter.write(temporal);
        assertThat(result).isEqualTo(temporal.toString());
    }

    static Stream<Arguments> temporalDataForConversion() {
        return Stream.of(
                arguments(LocalDateTime.now()),
                arguments(LocalDate.now()),
                arguments(LocalTime.now()),
                arguments(Year.now()),
                arguments(YearMonth.now()),
                arguments(ZonedDateTime.now())
        );
    }
}
