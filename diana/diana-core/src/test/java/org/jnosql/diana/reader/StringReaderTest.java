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

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class StringReaderTest {

    private ValueReader valueReader;

    @BeforeEach
    public void init() {
        valueReader = new StringReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.isCompatible(String.class));
        assertTrue(valueReader.isCompatible(CharSequence.class));
        assertFalse(valueReader.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        StringBuilder stringBuilder = new StringBuilder("sb");

        assertEquals(stringBuilder, valueReader.read(CharSequence.class, stringBuilder));
        assertEquals(stringBuilder.toString(), valueReader.read(String.class, stringBuilder));

        assertEquals("10", valueReader.read(CharSequence.class, 10));
        assertEquals("10.0", valueReader.read(String.class, 10.00));
        assertEquals("10", valueReader.read(CharSequence.class, "10"));
        assertEquals("10", valueReader.read(String.class, "10"));
    }


}
