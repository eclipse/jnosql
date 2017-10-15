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

import org.jnosql.diana.api.ValueReader;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class EnumReaderTest {

    private ValueReader valueReader;

    @Before
    public void init() {
        valueReader = new EnumValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.isCompatible(Enum.class));
        assertTrue(valueReader.isCompatible(ExampleNumber.class));
        assertFalse(valueReader.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        ExampleNumber value = ExampleNumber.ONE;
        assertEquals(value, valueReader.read(ExampleNumber.class, value));
        assertEquals(value, valueReader.read(ExampleNumber.class, 0));
        assertEquals(value, valueReader.read(ExampleNumber.class, "ONE"));
        assertEquals(ExampleNumber.TWO, valueReader.read(ExampleNumber.class, 1));
        assertEquals(ExampleNumber.TWO, valueReader.read(ExampleNumber.class, "TWO"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErrorInIndex() {
        valueReader.read(ExampleNumber.class, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErrorInName() {
        valueReader.read(ExampleNumber.class, "FOUR");
    }


    enum ExampleNumber {
        ONE, TWO
    }


}
