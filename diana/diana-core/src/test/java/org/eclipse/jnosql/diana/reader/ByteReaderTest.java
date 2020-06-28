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

package org.eclipse.jnosql.diana.reader;

import jakarta.nosql.ValueReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ByteReaderTest {

    private ValueReader valueReader;

    @BeforeEach
    public void init() {
        valueReader = new ByteReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.test(Byte.class));
        assertFalse(valueReader.test(AtomicBoolean.class));
        assertFalse(valueReader.test(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Byte number = (byte) 10;
        assertEquals(number, valueReader.read(Byte.class, 10.00));
        assertEquals(Byte.valueOf((byte) 10), valueReader.read(Byte.class, 10.00));
        assertEquals(Byte.valueOf((byte) 10), valueReader.read(Byte.class, "10"));
    }


}
