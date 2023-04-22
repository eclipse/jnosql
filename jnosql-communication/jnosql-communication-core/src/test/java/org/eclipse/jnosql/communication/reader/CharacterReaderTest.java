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

class CharacterReaderTest {

    private final ValueReader valueReader = new CharacterReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertThat(valueReader.test(Character.class)).isTrue();
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(valueReader.test(AtomicBoolean.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert the value to Character")
    void shouldConvert() {
        assertSoftly(softly -> {
            softly.assertThat(valueReader.read(Character.class, 'o')).as("char conversion").isEqualTo('o');
            softly.assertThat(valueReader.read(Character.class, 10.00)).as("Integer conversion").isEqualTo(Character.valueOf((char) 10));
            softly.assertThat(valueReader.read(Character.class, "10")).as("char conversion").isEqualTo(Character.valueOf('1'));
            softly.assertThat(valueReader.read(Character.class, "")).as("Character conversion")
                    .isEqualTo(Character.valueOf(Character.MIN_VALUE));
        });
    }
}
