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
 *
 */

package org.eclipse.jnosql.communication.reader;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZonedDateTimeReaderTest {

    private ZonedDateTimeReader dateReader;

    @BeforeEach
    public void init() {
        dateReader = new ZonedDateTimeReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(dateReader.test(ZonedDateTime.class));
    }

    @Test
    public void shouldConvert() {
        final ZonedDateTime now = ZonedDateTime.now();
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();

        assertEquals(now, dateReader.read(ZonedDateTime.class, now));
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()), dateReader.read(ZonedDateTime.class, date));
        assertEquals(calendar.toInstant().atZone(ZoneId.systemDefault()), dateReader.read(ZonedDateTime.class, calendar));
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()), dateReader.read(ZonedDateTime.class, date.getTime()));
    }
}
