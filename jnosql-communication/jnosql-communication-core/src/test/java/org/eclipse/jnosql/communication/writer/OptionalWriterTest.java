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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OptionalWriterTest {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private final ValueWriter<Optional, String> valueWriter = new OptionalValueWriter();

    @Test
    @DisplayName("Should be able to verify compatibility with Optional")
    void shouldVerifyCompatibility() {
        assertThat(valueWriter.test(Optional.class)).isTrue();
    }

    @Test
    @DisplayName("Should be able to verify the class incompatibility")
    void shouldVerifyIncompatibility() {
        assertThat(valueWriter.test(Boolean.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert the result")
    void shouldConvert() {
        String diana = "diana";
        Optional<String> optional = Optional.of(diana);
        String result = valueWriter.write(optional);

        assertThat(diana).isEqualTo(result);
    }
}
