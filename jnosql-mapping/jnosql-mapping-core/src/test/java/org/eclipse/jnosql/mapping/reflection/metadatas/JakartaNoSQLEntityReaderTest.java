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
package org.eclipse.jnosql.mapping.reflection.metadatas;

import jakarta.nosql.mapping.Entity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JakartaNoSQLEntityReaderTest {

    private EntityAnnotationReader reader;

    @BeforeEach
    public void setUp() {
        this.reader = new JakartaNoSQLEntityReader();
    }
    @Test
    public void shouldTestEntities() {
        Assertions.assertTrue(reader.test(Person.class));
        Assertions.assertTrue(reader.test(Car.class));
        Assertions.assertFalse(reader.test(Animal.class));
    }

    @Test
    public void shouldReadFromAnnotation() {
        String name = reader.name(Car.class);
        Assertions.assertEquals("vehicle", name);
    }

    @Test
    public void shouldUseSimpleName() {
        String name = reader.name(Person.class);
        Assertions.assertEquals(Person.class.getSimpleName(), name);
    }

    @Entity
    private static final class Person {

    }

    private static final class Animal {

    }

    @Entity("vehicle")
    private static final class Car {

    }
}