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

package org.eclipse.jnosql.communication.reader;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OffsetDateTimeReaderTest {

    private OffsetDateTimeReader dateReader;

    @BeforeEach
    public void init() {
        dateReader = new OffsetDateTimeReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertFalse(dateReader.test(LocalTime.class));
        assertTrue(dateReader.test(OffsetDateTime.class));
    }

    @Test
    public void shouldConvert() {
        final OffsetDateTime now = OffsetDateTime.now();
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        assertEquals(now, dateReader.read(LocalTime.class, now));
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                dateReader.read(OffsetTime.class, date));
        assertEquals(calendar.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                dateReader.read(OffsetTime.class, calendar));
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime(),
                dateReader.read(OffsetTime.class, date.getTime()));
    }
}
