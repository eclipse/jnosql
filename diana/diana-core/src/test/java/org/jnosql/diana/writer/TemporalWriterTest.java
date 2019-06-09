/*
 *
 *  Copyright (c) 2017 Otávio Santana and others
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
 *
 */

package org.jnosql.diana.writer;

import jakarta.nosql.ValueWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemporalWriterTest {


    private ValueWriter<Temporal, String> valueWriter;

    @BeforeEach
    public void setUp() {
        valueWriter = new TemporalValueWriter();
    }

    @Test
    public void shouldVerifyCompatibility() {
        assertTrue(valueWriter.isCompatible(Temporal.class));
        assertTrue(valueWriter.isCompatible(LocalDate.class));
        assertTrue(valueWriter.isCompatible(LocalDateTime.class));
        assertTrue(valueWriter.isCompatible(LocalTime.class));
        assertTrue(valueWriter.isCompatible(Year.class));
        assertTrue(valueWriter.isCompatible(YearMonth.class));
        assertTrue(valueWriter.isCompatible(ZonedDateTime.class));
        assertFalse(valueWriter.isCompatible(Boolean.class));

    }

    @Test
    public void shouldConvertLocalDate() {
        LocalDate now = LocalDate.now();
        String result = valueWriter.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, LocalDate.parse(result));
    }

    @Test
    public void shouldConvertLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String result = valueWriter.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, LocalDateTime.parse(result));
    }

    @Test
    public void shouldConvertLocalTime() {
        LocalTime now = LocalTime.now();
        String result = valueWriter.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, LocalTime.parse(result));
    }


    @Test
    public void shouldConvertYear() {
        Year now = Year.now();
        String result = valueWriter.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, Year.parse(result));
    }

    @Test
    public void shouldConvertYearMonth() {
        YearMonth now = YearMonth.now();
        String result = valueWriter.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, YearMonth.parse(result));
    }

    @Test
    public void shouldConvertZonedDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        String result = valueWriter.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, ZonedDateTime.parse(result));
    }

}