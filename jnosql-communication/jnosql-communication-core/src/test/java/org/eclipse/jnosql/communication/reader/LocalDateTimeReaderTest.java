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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

class LocalDateTimeReaderTest {

    private final LocalDateTimeReader dateReader = new LocalDateTimeReader();

    @Test
    @DisplayName("Should be compatible")
    void shouldValidateCompatibility() {
        assertThat(dateReader.test(LocalDateTime.class)).isTrue();
    }

    @Test
    @DisplayName("Should be incompatible")
    void shouldValidateIncompatibility() {
        assertThat(dateReader.test(String.class)).isFalse();
    }

    //TODO: extra review here as the inversion of the assertions was hiding possible cast exceptions
    @Test
    @DisplayName("Should be able to convert the value to LocalDate")
    void shouldConvert() {
        final LocalDateTime now = LocalDateTime.now();
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        assertSoftly(softly -> {
            softly.assertThat(dateReader.read(LocalDateTime.class, now)).as("LocalDateTime conversion").isEqualTo(now);

            softly.assertThat(dateReader.read(LocalDateTime.class, now)).as("Date conversion")
                    .isEqualToIgnoringSeconds(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            softly.assertThat(dateReader.read(LocalDateTime.class, now)).as("Calendar conversion")
                    .isEqualToIgnoringSeconds(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            softly.assertThat(dateReader.read(LocalDateTime.class, date.getTime())).as("Number conversion")
                    .isEqualToIgnoringSeconds(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());

            softly.assertThat(dateReader.read(LocalDateTime.class, now.toString())).as("Default conversion")
                    .isEqualToIgnoringSeconds(now);
        });
    }
}