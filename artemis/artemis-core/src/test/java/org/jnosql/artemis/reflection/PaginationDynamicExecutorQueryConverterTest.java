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

import jakarta.nosql.mapping.Page;
import org.jnosql.artemis.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class PaginationDynamicExecutorQueryConverterTest {


    private DynamicReturn<Person> dynamic;

    private DynamicExecutorQueryConverter converter;

    @Mock
    private Page<Person> page;


    @BeforeEach
    public void setUp() {
        this.converter = new PaginationDynamicExecutorQueryConverter();
    }

    @Test
    public void shouldReturnInstance() {

        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.of(ada))
                .withListPagination(p -> Collections.emptyList())
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Person person = converter.toInstance(dynamic);
        Assertions.assertNotNull(person);
        assertEquals(ada, person);
    }

    @Test
    public void shouldReturnNullAsInstance() {
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.emptyList())
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Person person = converter.toInstance(dynamic);
        Assertions.assertNull(person);
    }

    @Test
    public void shouldReturnOptional() {

        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.of(ada))
                .withListPagination(p -> Collections.emptyList())
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Optional<Person> person = converter.toOptional(dynamic);
        Assertions.assertNotNull(person);
        Assertions.assertTrue(person.isPresent());
        assertEquals(ada, person.get());
    }

    @Test
    public void shouldReturnList() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        List<Person> person = converter.toList(dynamic);
        Assertions.assertNotNull(person);
        assertFalse(person.isEmpty());
        assertEquals(ada, person.get(0));
    }

    @Test
    public void shouldReturnSet() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Set<Person> person = converter.toSet(dynamic);
        Assertions.assertNotNull(person);
        assertFalse(person.isEmpty());
        assertEquals(ada, person.stream().findFirst().get());
    }


    @Test
    public void shouldReturnLinkedList() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        LinkedList<Person> person = converter.toLinkedList(dynamic);
        Assertions.assertNotNull(person);
        assertFalse(person.isEmpty());
        assertEquals(ada, person.stream().findFirst().get());
    }

    @Test
    public void shouldReturnTreeSet() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        TreeSet<Person> person = converter.toTreeSet(dynamic);
        Assertions.assertNotNull(person);
        assertFalse(person.isEmpty());
        assertEquals(ada, person.stream().findFirst().get());
    }

    @Test
    public void shouldReturnStream() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Stream<Person> person = converter.toStream(dynamic);
        Assertions.assertNotNull(person);
        assertEquals(ada, person.findFirst().get());
    }


    @Test
    public void shouldReturnDefault() {
        Person ada = new Person("Ada");
        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();
        Object person = converter.toDefault(dynamic);
        Assertions.assertNotNull(person);
    }

    @Test
    public void shouldReturnErrorWhenUsePage() {

        Person ada = new Person("Ada");

        Mockito.when(page.getContent()).thenReturn(Collections.singletonList(ada));

        dynamic = DynamicReturn.builder()
                .withClassSource(Person.class)
                .withSingleResult(Optional::empty)
                .withList(Collections::emptyList)
                .withSingleResultPagination(p -> Optional.empty())
                .withListPagination(p -> Collections.singletonList(ada))
                .withMethodSource(Person.class.getDeclaredMethods()[0])
                .withPagination(Pagination.page(2).size(2))
                .withPage(p -> page)
                .build();

        Page<Person> personPage = converter.toPage(dynamic);
        List<Person> content = personPage.getContent();

        assertFalse(content.isEmpty());
        assertEquals(ada, content.get(0));
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