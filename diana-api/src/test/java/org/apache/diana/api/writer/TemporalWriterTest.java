/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.Temporal;

import static org.junit.Assert.*;

public class TemporalWriterTest {


    private WriterField<Temporal, String> writerField;

    @Before
    public void setUp() {
        writerField = new TemporalWriter();
    }

    @Test
    public void shouldVerifyCompatibility() {
        assertTrue(writerField.isCompatible(Temporal.class));
        assertTrue(writerField.isCompatible(LocalDate.class));
        assertTrue(writerField.isCompatible(LocalDateTime.class));
        assertTrue(writerField.isCompatible(LocalTime.class));
        assertTrue(writerField.isCompatible(Year.class));
        assertTrue(writerField.isCompatible(YearMonth.class));
        assertTrue(writerField.isCompatible(ZonedDateTime.class));
        assertFalse(writerField.isCompatible(Boolean.class));

    }

    @Test
    public void shouldConvertLocalDate() {
        LocalDate now = LocalDate.now();
        String result = writerField.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, LocalDate.parse(result));
    }

    @Test
    public void shouldConvertLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        String result = writerField.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, LocalDateTime.parse(result));
    }

    @Test
    public void shouldConvertLocalTime() {
        LocalTime now = LocalTime.now();
        String result = writerField.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, LocalTime.parse(result));
    }


    @Test
    public void shouldConvertYear() {
        Year now = Year.now();
        String result = writerField.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, Year.parse(result));
    }

    @Test
    public void shouldConvertYearMonth() {
        YearMonth now = YearMonth.now();
        String result = writerField.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, YearMonth.parse(result));
    }

    @Test
    public void shouldConvertZonedDateTime() {
        ZonedDateTime now = ZonedDateTime.now();
        String result = writerField.write(now);
        assertEquals(now.toString(), result);
        assertEquals(now, ZonedDateTime.parse(result));
    }

}