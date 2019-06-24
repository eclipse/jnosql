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

package org.jnosql.diana.reader;

import jakarta.nosql.ValueReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class YearReaderTest {

    private ValueReader valueReader;

    @BeforeEach
    public void init() {
        valueReader = new YearValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.isCompatible(Year.class));
        assertFalse(valueReader.isCompatible(String.class));
        assertFalse(valueReader.isCompatible(Long.class));
    }

    @Test
    public void shouldConvert() {
        Year year = Year.parse("2009");

        assertEquals(year, valueReader.read(Year.class, Year.parse("2009")));
        assertEquals(year, valueReader.read(String.class, "2009"));
        assertEquals(year, valueReader.read(Integer.class, 2009));
        assertEquals(year, valueReader.read(Long.class, 2009));
    }


}
