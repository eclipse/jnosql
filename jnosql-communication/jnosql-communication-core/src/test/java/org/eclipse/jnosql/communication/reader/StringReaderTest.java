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
package org.eclipse.jnosql.communication.reader;

import org.eclipse.jnosql.communication.ValueReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

public class StringReaderTest {

    private final ValueReader valueReader = new StringReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertSoftly(softly -> {
            softly.assertThat(valueReader.test(String.class)).as("String compatible").isTrue();
            softly.assertThat(valueReader.test(CharSequence.class)).as("CharSequence compatible").isTrue();
        });
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(valueReader.test(AtomicBoolean.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert")
    void shouldConvert() {
        StringBuilder stringBuilder = new StringBuilder("sb");

        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(CharSequence.class, stringBuilder)).as("CharSequence compatible").isEqualTo(stringBuilder);
            softly.assertThat(valueReader.read(String.class, stringBuilder)).as("String compatible").isEqualTo(stringBuilder.toString());
            softly.assertThat(valueReader.read(CharSequence.class, 10)).as("Integer compatible").isEqualTo("10");
            softly.assertThat(valueReader.read(CharSequence.class, 10.0)).as("Number compatible").isEqualTo("10.0");
            softly.assertThat(valueReader.read(CharSequence.class, 10)).as("CharSequence compatible").isEqualTo("10");
            softly.assertThat(valueReader.read(String.class, 10)).as("String compatible").isEqualTo("10");
        });
    }
}
