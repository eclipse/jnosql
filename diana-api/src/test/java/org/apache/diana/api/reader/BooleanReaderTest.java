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

package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class BooleanReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new BooleanReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(AtomicBoolean.class));
        assertTrue(readerField.isCompatible(Boolean.class));
        assertFalse(readerField.isCompatible(Number.class));
    }

    @Test
    public void shouldConvert() {
        AtomicBoolean atomicBooleanTrue = new AtomicBoolean(true);
        AtomicBoolean atomicBooleanFalse = new AtomicBoolean(false);

        assertEquals(atomicBooleanTrue, readerField.read(AtomicBoolean.class, atomicBooleanTrue));
        assertEquals(atomicBooleanTrue.get(), readerField.read(AtomicBoolean.class, 10).get());
        assertEquals(atomicBooleanTrue.get(), readerField.read(AtomicBoolean.class, -1).get());
        assertEquals(atomicBooleanFalse.get(), readerField.read(AtomicBoolean.class, 0).get());
        assertEquals(atomicBooleanTrue.get(), readerField.read(AtomicBoolean.class, "true").get());
        assertEquals(atomicBooleanFalse.get(), readerField.read(AtomicBoolean.class, "false").get());
        assertEquals(atomicBooleanTrue.get(), readerField.read(AtomicBoolean.class, true).get());
        assertEquals(atomicBooleanFalse.get(), readerField.read(AtomicBoolean.class, false).get());


        assertEquals(Boolean.TRUE, readerField.read(Boolean.class, atomicBooleanTrue));
        assertEquals(Boolean.TRUE, readerField.read(Boolean.class, 10));
        assertEquals(Boolean.TRUE, readerField.read(Boolean.class, -1));
        assertEquals(Boolean.FALSE, readerField.read(Boolean.class, 0));
        assertEquals(Boolean.TRUE, readerField.read(Boolean.class, "true"));
        assertEquals(Boolean.FALSE, readerField.read(Boolean.class, "false"));
        assertEquals(Boolean.TRUE, readerField.read(Boolean.class, true));
        assertEquals(Boolean.FALSE, readerField.read(Boolean.class, false));

    }


}
