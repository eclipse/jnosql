/*
 * Copyright 2017 Otavio Santana and others
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 */

package org.jnosql.diana.api.reader;

import org.jnosql.diana.api.ValueReader;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class DoubleReaderTest {

    private ValueReader valueReader;

    @Before
    public void init() {
        valueReader = new DoubleValueReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(valueReader.isCompatible(Double.class));
        assertFalse(valueReader.isCompatible(AtomicBoolean.class));
        assertFalse(valueReader.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Double number = 10D;
        assertEquals(number, valueReader.read(Double.class, number));
        assertEquals(Double.valueOf(10D), valueReader.read(Double.class, 10.00));
        assertEquals(Double.valueOf(10D), valueReader.read(Double.class, "10"));
    }


}
