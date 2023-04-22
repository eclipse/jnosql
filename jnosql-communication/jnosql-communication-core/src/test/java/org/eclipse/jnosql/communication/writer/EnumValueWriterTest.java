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
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.List;

import static java.time.Month.JANUARY;
import static org.assertj.core.api.Assertions.assertThat;

class EnumValueWriterTest {

    private final ValueWriter<Enum<?>, String> valueWriter = new EnumValueWriter();

    @ParameterizedTest(name = "must be compatible with {0}")
    @DisplayName("Should verify compatibility with enums")
    @ValueSource(classes = {Month.class, DayOfWeek.class})
    void shouldVerifyCompatibility(Class<?> enumClass) {
        assertThat(valueWriter.test(enumClass)).isTrue();
    }

    @ParameterizedTest(name = "must be compatible with {0}")
    @DisplayName("Should verify incompatibility with enums")
    @ValueSource(classes = {Integer.class, List.class})
    void shouldNotVerifyCompatibility(Class<?> notEnumClass) {
        assertThat(valueWriter.test(notEnumClass)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert an enum")
    void shouldConvert() {
        String result = valueWriter.write(JANUARY);
        assertThat(JANUARY.name()).isEqualTo(result);
    }
}
