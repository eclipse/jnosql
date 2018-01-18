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

package org.jnosql.diana.api.reader;


import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.Assert.assertTrue;

public class LocalDateReaderTest {

    private LocalDateValueReader dateReader;

    @Before
    public void init() {
        dateReader = new LocalDateValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(dateReader.isCompatible(LocalDate.class));
    }

    @Test
    public void shouldConvert() {
        final LocalDate now = LocalDate.now();
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        assertEquals(now, dateReader.read(LocalDate.class, now));
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), dateReader.read(LocalDate.class, date));
        assertEquals(calendar.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), dateReader.read(LocalDate.class, calendar));
        assertEquals(date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), dateReader.read(LocalDate.class, date.getTime()));
    }
}
