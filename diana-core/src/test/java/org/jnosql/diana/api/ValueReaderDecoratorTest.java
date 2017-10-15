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
