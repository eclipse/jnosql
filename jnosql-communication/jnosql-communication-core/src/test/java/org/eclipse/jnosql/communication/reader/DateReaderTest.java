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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class DateReaderTest {

    private final DateReader dateReader = new DateReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertThat(dateReader.test(Date.class)).isTrue();
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(dateReader.test(String.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert the value to Date")
    void shouldConvert() {
        long milliseconds = new Date().getTime();
        Date date = new Date();

        assertSoftly(softly -> {
            softly.assertThat(dateReader.read(Date.class, milliseconds).getTime()).as("long conversion").isEqualTo(milliseconds);
            softly.assertThat(dateReader.read(Date.class, date)).as("Date conversion").isEqualTo(date);
        });
    }
}
