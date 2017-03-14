/*
 * Copyright 2017 Otavio Santana and others
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
