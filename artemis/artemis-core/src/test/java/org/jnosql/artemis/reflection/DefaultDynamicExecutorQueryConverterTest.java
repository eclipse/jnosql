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

import jakarta.nosql.mapping.DynamicQueryException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;


class DefaultDynamicExecutorQueryConverterTest {

    private DynamicReturn<Person> dynamic;

    private DynamicExecutorQueryConverter converter;


    @BeforeEach
    public void setUp() {
        this.converter = new DefaultDynamicExecutorQueryConverter();
    }

    @Test
    public void shouldReturnInstance() {

        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(() -> Optional.of(ada))
                .withClassSource(Person.class)
                .withResult(Collections::emptyList)
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Person person = converter.toInstance(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(ada, person);
    }

    @Test
    public void shouldReturnNullAsInstance() {
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(Collections::emptyList)
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Person person = converter.toInstance(dynamic);
        Assertions.assertNull(person);
    }

    @Test
    public void shouldReturnOptional() {

        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(() -> Optional.of(ada))
                .withClassSource(Person.class)
                .withResult(Collections::emptyList)
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Optional<Person> person = converter.toOptional(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isPresent());
        Assertions.assertEquals(ada, person.get());
    }

    @Test
    public void shouldReturnList() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        List<Person> person = converter.toList(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertFalse(person.isEmpty());
        Assertions.assertEquals(ada, person.get(0));
    }

    @Test
    public void shouldReturnSet() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Set<Person> person = converter.toSet(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertFalse(person.isEmpty());
        Assertions.assertEquals(ada, person.stream().findFirst().get());
    }


    @Test
    public void shouldReturnLinkedList() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        LinkedList<Person> person = converter.toLinkedList(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertFalse(person.isEmpty());
        Assertions.assertEquals(ada, person.stream().findFirst().get());
    }

    @Test
    public void shouldReturnTreeSet() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        TreeSet<Person> person = converter.toTreeSet(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertFalse(person.isEmpty());
        Assertions.assertEquals(ada, person.stream().findFirst().get());
    }

    @Test
    public void shouldReturnStream() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Stream<Person> person = converter.toStream(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertEquals(ada, person.findFirst().get());
    }


    @Test
    public void shouldReturnDefault() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Object person = converter.toDefault(dynamic);
        Assertions.assertNotNull(person);
    }

    @Test
    public void shouldReturnErrorWhenUsePage() {

        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withSingleResult(Optional::empty)
                .withClassSource(Person.class)
                .withResult(() -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .build();
        Assertions.assertThrows(DynamicQueryException.class, () -> converter.toPage(dynamic));
    }


    private static class Person implements Comparable<Person> {

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

        @Override
        public int compareTo(Person o) {
            return name.compareTo(o.name);
        }
    }
}