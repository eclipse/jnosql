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

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class LocalTimeReaderTest {

    private final LocalTimeReader dateReader = new LocalTimeReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertThat(dateReader.test(LocalTime.class)).isTrue();
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(dateReader.test(Integer.class)).isFalse();
    }

    @Test
    @DisplayName("Should be able to convert the value to LocalTime")
    void shouldConvert() {
        final LocalTime now = LocalTime.now();
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        assertSoftly(softly -> {
            softly.assertThat(dateReader.read(LocalTime.class, now)).as("LocalTime conversion").isEqualTo(now);

            softly.assertThat(dateReader.read(LocalTime.class, date)).as("Date conversion")
                    .isEqualTo(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

            softly.assertThat(dateReader.read(LocalTime.class, calendar)).as("Calendar conversion")
                    .isEqualTo(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

            softly.assertThat(dateReader.read(LocalTime.class, date.getTime())).as("Number conversion")
                    .isEqualTo(date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime());

            softly.assertThat(dateReader.read(LocalTime.class, now.toString())).as("Default conversion").isEqualTo(now);
        });
    }
}
