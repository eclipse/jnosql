/*
 *  Copyright (c) 2022 Contributors to the Eclipse Foundation
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstructorComparableTest {


    @Test
    public void shouldReturnDefaultConstructor(){
        Constructor<?> constructor = Stream.of(Person.class.getDeclaredConstructors()).min(ConstructorComparable.INSTANCE).orElseThrow();

        Assertions.assertNotNull(constructor);
    }

    @Test
    public void shouldReturnDefault() {
        Constructor<?> constructor = Stream.of(Animal.class.getDeclaredConstructors())
                .min(ConstructorComparable.INSTANCE).orElseThrow();

        Assertions.assertNotNull(constructor);
        assertEquals(0, constructor.getParameterCount());
    }

    static class Person {
        private String name;
    }

    static class Animal {
        Animal(String name) {

        }
        Animal(int age) {

        }

        Animal(){

        }
    }


}