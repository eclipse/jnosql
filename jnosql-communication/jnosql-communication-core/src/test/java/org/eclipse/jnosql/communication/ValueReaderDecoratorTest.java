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

package org.eclipse.jnosql.communication;

import jakarta.nosql.ValueReaderDecorator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ValueReaderDecoratorTest {

    private final ValueReaderDecorator serviceLoader = ValueReaderDecorator.getInstance();

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

    @Test
    public void shouldReturnErrorWhenTypeIsNotSupported() {
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            Bean name = serviceLoader.read(Bean.class, "name");
        });
    }

    @Test
    public void shouldReturnIfIsCompatible() {
        assertTrue(serviceLoader.test(Integer.class));
    }

    @Test
    public void shouldReturnIfIsNotCompatible() {
        assertFalse(serviceLoader.test(Bean.class));
    }


    static class Bean {
        private String name;

        Bean() {
            this.name = "name";
        }
    }

}
