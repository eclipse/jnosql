/*
 * Copyright 2017 Eclipse Foundation
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jnosql.diana.api.writer;

import org.jnosql.diana.api.ValueWriter;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.Temporal;

import static org.junit.Assert.*;

public class TemporalWriterTest {


    private ValueWriter<Temporal, String> valueWriter;

    @Before
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