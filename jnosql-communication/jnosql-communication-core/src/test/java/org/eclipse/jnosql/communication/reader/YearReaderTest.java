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
        valueReader = new YearReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.test(Year.class));
        assertFalse(valueReader.test(String.class));
        assertFalse(valueReader.test(Long.class));
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
