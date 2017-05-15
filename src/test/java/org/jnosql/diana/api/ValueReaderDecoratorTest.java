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

package org.jnosql.diana.api;

import org.junit.Test;

import static org.junit.Assert.*;


public class ValueReaderDecoratorTest {

    private ValueReaderDecorator serviceLoader = ValueReaderDecorator.getInstance();


    @Test
    public void shouldConvert() {
        Number convert = serviceLoader.read(Number.class, "10D");
        assertEquals(10D, convert);
    }

    @Test
    public void shouldCastObject() {
        Bean name = serviceLoader.read(Bean.class, new Bean());
        assertEquals(name.getClass(), Bean.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldReturnErrorWhenTypeIsNotSupported() {
        Bean name = serviceLoader.read(Bean.class, "name");
    }

    @Test
    public void shouldReturnIfIsCompatible() {
        assertTrue(serviceLoader.isCompatible(Integer.class));
    }

    @Test
    public void shouldReturnIfIsNotCompatible() {
        assertFalse(serviceLoader.isCompatible(Bean.class));
    }


    class Bean {
        private String name;

        Bean() {
            this.name = "name";
        }
    }

}
