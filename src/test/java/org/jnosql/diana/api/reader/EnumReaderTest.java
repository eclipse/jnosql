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
