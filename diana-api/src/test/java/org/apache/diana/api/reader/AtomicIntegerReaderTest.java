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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;


public class AtomicIntegerReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new AtomicIntegerReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(AtomicInteger.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        AtomicInteger integer = new AtomicInteger(10);
        assertEquals(integer, readerField.read(AtomicInteger.class, integer));
        assertEquals(integer.get(), readerField.read(AtomicInteger.class, 10.00).get());
        assertEquals(integer.get(), readerField.read(AtomicInteger.class, "10").get());
    }


}
