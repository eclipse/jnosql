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


public class BooleanReaderTest {

    private ValueReader valueReader;

    @Before
    public void init() {
        valueReader = new BooleanValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.isCompatible(AtomicBoolean.class));
        assertTrue(valueReader.isCompatible(Boolean.class));
        assertFalse(valueReader.isCompatible(Number.class));
    }

    @Test
    public void shouldConvert() {
        AtomicBoolean atomicBooleanTrue = new AtomicBoolean(true);
        AtomicBoolean atomicBooleanFalse = new AtomicBoolean(false);

        assertEquals(atomicBooleanTrue, valueReader.read(AtomicBoolean.class, atomicBooleanTrue));
        assertEquals(atomicBooleanTrue.get(), valueReader.read(AtomicBoolean.class, 10).get());
        assertEquals(atomicBooleanTrue.get(), valueReader.read(AtomicBoolean.class, -1).get());
        assertEquals(atomicBooleanFalse.get(), valueReader.read(AtomicBoolean.class, 0).get());
        assertEquals(atomicBooleanTrue.get(), valueReader.read(AtomicBoolean.class, "true").get());
        assertEquals(atomicBooleanFalse.get(), valueReader.read(AtomicBoolean.class, "false").get());
        assertEquals(atomicBooleanTrue.get(), valueReader.read(AtomicBoolean.class, true).get());
        assertEquals(atomicBooleanFalse.get(), valueReader.read(AtomicBoolean.class, false).get());


        assertEquals(Boolean.TRUE, valueReader.read(Boolean.class, atomicBooleanTrue));
        assertEquals(Boolean.TRUE, valueReader.read(Boolean.class, 10));
        assertEquals(Boolean.TRUE, valueReader.read(Boolean.class, -1));
        assertEquals(Boolean.FALSE, valueReader.read(Boolean.class, 0));
        assertEquals(Boolean.TRUE, valueReader.read(Boolean.class, "true"));
        assertEquals(Boolean.FALSE, valueReader.read(Boolean.class, "false"));
        assertEquals(Boolean.TRUE, valueReader.read(Boolean.class, true));
        assertEquals(Boolean.FALSE, valueReader.read(Boolean.class, false));

    }


}
