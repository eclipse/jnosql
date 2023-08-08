/*
 *  Copyright (c) 2023 Contributors to the Eclipse Foundation
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
 */
package org.eclipse.jnosql.mapping.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Arrays;

public class ConstructorEventTest {

    @Test
    public void shouldCreateConstructorEvent() throws NoSuchMethodException {
        // Create a sample class to get the constructor
        Class<?> sampleClass = SampleClass.class;

        // Get the constructor with parameter types
        Constructor<?> constructor = sampleClass.getConstructor(int.class, String.class);

        // Sample parameters
        int intValue = 42;
        String stringValue = "Hello, World!";

        // Create the ConstructorEvent instance
        ConstructorEvent constructorEvent = ConstructorEvent.of(constructor, new Object[]{intValue, stringValue});

        // Access the constructor and parameters
        Constructor<?> eventConstructor = constructorEvent.constructor();
        Object[] eventParams = constructorEvent.params();

        Assertions.assertEquals(constructor, eventConstructor);
        Assertions.assertArrayEquals(new Object[]{intValue, stringValue}, eventParams);
    }

    @Test
    public void shouldThrowNullPointerException() {
        Assertions.assertThrows(NullPointerException.class, () -> ConstructorEvent.of(null, new Object[]{1}));
        Assertions.assertThrows(NullPointerException.class, () -> ConstructorEvent.of(ConstructorEventTest.class.getDeclaredConstructors()[0], null));
    }

    public static class SampleClass {
        public SampleClass(int intValue, String stringValue) {
            // Sample constructor
        }
    }
}