/*
 *  Copyright (c) 2019 Otávio Santana and others
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
package org.jnosql.artemis.reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.requireNonNull;


class DefaultDynamicExecuteQueryConverterTest {

    private DynamicReturn<Person> dynamic;

    private DynamicExecuteQueryConverter converter;


    @BeforeEach
    public void setUp() {
        this.converter = new DefaultDynamicExecuteQueryConverter();
    }

    @Test
    public void shouldReturnInstance() {

        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(() -> Optional.of(ada))
                .withClassSource(Person.class)
                .withList(() -> Collections.emptyList())
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Person person = converter.toInstance(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(ada, person);
    }

    @Test
    public void shouldReturnNullAsInstance() {
        dynamic = DynamicReturn.builder()
                .withSingleResult(() -> Optional.empty())
                .withClassSource(Person.class)
                .withList(() -> Collections.emptyList())
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Person person = converter.toInstance(dynamic);
        Assertions.assertNull(person);
    }


    private static class Person {

        private String name;

        public Person(String name) {
            this.name = name;
        }

        public Person() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Person person = (Person) o;
            return Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(name);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }
}